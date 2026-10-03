$ErrorActionPreference = "Stop"
$word = New-Object -ComObject Word.Application
$word.Visible = $false

try {
    $docPath = "C:\Users\S.RAJIT\.gemini\antigravity\scratch\lms\LearnSphere_LMS_PBL_Report.docx"
    Write-Host "Opening document: $docPath"
    $doc = $word.Documents.Open($docPath)
    
    # 1. Update the manual Table of Contents
    # Let's iterate over ALL tables in the document.
    $tableCount = $doc.Tables.Count
    Write-Host "Total tables: $tableCount"
    
    for ($t = 1; $t -le $tableCount; $t++) {
        $table = $doc.Tables.Item($t)
        
        # Check if the table is the TOC, List of Tables, or List of Figures
        if ($table.Rows.Count -ge 2) {
            $headerText = $table.Cell(1, 2).Range.Text.Trim(" `r`n`a").ToUpper()
            
            if ($headerText -eq "TITLE" -or $headerText -eq "DESCRIPTION") {
                # This looks like one of our manual index tables!
                Write-Host "Processing table $t with header '$headerText'"
                
                for ($i = 2; $i -le $table.Rows.Count; $i++) {
                    $cellTitle = $table.Cell($i, 2).Range.Text.Trim(" `r`n`a")
                    if (-not [string]::IsNullOrWhiteSpace($cellTitle)) {
                        # Search for this title in the document
                        $searchRange = $doc.Content
                        $findTitle = $searchRange.Find
                        $findTitle.Text = $cellTitle
                        
                        # We want to skip this table itself.
                        $searchRange.Start = $table.Range.End
                        
                        # Wait, what if the target is BEFORE the table? (e.g. ABSTRACT is before TOC).
                        # We need to search the entire document EXCEPT this table.
                        # It's easier to just search from the start of the document and take the first match that is NOT in the table.
                        $foundReal = $false
                        $searchRange.Start = 0
                        $searchRange.End = $doc.Content.End
                        
                        while ($findTitle.Execute()) {
                            # Check if the match is inside the current table
                            if ($searchRange.Start -ge $table.Range.Start -and $searchRange.End -le $table.Range.End) {
                                # It's inside the table, keep searching
                                $searchRange.Start = $searchRange.End
                                $searchRange.End = $doc.Content.End
                            } else {
                                $foundReal = $true
                                break
                            }
                        }
                        
                        if ($foundReal) {
                            $pageNum = $searchRange.Information(3) # wdActiveEndAdjustedPageNumber
                            $secNum = $searchRange.Information(2) # wdActiveEndSectionNumber
                            $sec = $doc.Sections.Item($secNum)
                            $style = $sec.Footers.Item(1).PageNumbers.NumberStyle
                            
                            $pageStr = $pageNum.ToString()
                            if ($style -eq 2) { # Lowercase Roman
                                $romans = @{1='i'; 2='ii'; 3='iii'; 4='iv'; 5='v'; 6='vi'; 7='vii'; 8='viii'; 9='ix'; 10='x'; 11='xi'; 12='xii'}
                                if ($romans.ContainsKey($pageNum)) {
                                    $pageStr = $romans[$pageNum]
                                }
                            }
                            
                            $table.Cell($i, 3).Range.Text = $pageStr
                            Write-Host "Updated '$cellTitle' to page $pageStr"
                        } else {
                            Write-Host "Warning: Could not find target '$cellTitle' in document outside the TOC."
                        }
                    }
                }
            }
        }
    }
    
    $doc.Save()
    Write-Host "Document saved successfully."
} catch {
    Write-Host "An error occurred: $_"
} finally {
    if ($doc) { $doc.Close(-1) }
    if ($word) { $word.Quit() }
    [System.GC]::Collect()
    [System.GC]::WaitForPendingFinalizers()
}
