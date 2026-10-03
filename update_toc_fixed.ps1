$ErrorActionPreference = "Stop"
$word = New-Object -ComObject Word.Application
$word.Visible = $false
$word.DisplayAlerts = 0 # wdAlertsNone

try {
    $docPath = "C:\Users\S.RAJIT\.gemini\antigravity\scratch\lms\LearnSphere_LMS_PBL_Report.docx"
    Write-Host "Opening document: $docPath"
    
    # Open(FileName, ConfirmConversions, ReadOnly, AddToRecentFiles, PasswordDocument, PasswordTemplate, Revert, WritePasswordDocument, WritePasswordTemplate, Format, Encoding, Visible)
    $doc = $word.Documents.Open($docPath, $false, $false, $false, [Type]::Missing, [Type]::Missing, $true)
    
    $tableCount = $doc.Tables.Count
    Write-Host "Total tables: $tableCount"
    
    for ($t = 1; $t -le $tableCount; $t++) {
        $table = $doc.Tables.Item($t)
        
        if ($table.Rows.Count -ge 2) {
            $headerText = $table.Cell(1, 2).Range.Text.Trim(" `r`n`a").ToUpper()
            
            if ($headerText -eq "TITLE" -or $headerText -eq "DESCRIPTION") {
                Write-Host "Processing index table $t..."
                
                for ($i = 2; $i -le $table.Rows.Count; $i++) {
                    $cellTitle = $table.Cell($i, 2).Range.Text.Trim(" `r`n`a")
                    
                    if (-not [string]::IsNullOrWhiteSpace($cellTitle)) {
                        
                        $searchRange = $doc.Range($table.Range.End, $doc.Content.End)
                        $findTitle = $searchRange.Find
                        $findTitle.Text = $cellTitle
                        
                        $found = $findTitle.Execute()
                        
                        if (-not $found) {
                            $searchRange = $doc.Range(0, $table.Range.Start)
                            $findTitle = $searchRange.Find
                            $findTitle.Text = $cellTitle
                            $found = $findTitle.Execute()
                        }
                        
                        if ($found) {
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
