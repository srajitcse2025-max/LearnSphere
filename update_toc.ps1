$ErrorActionPreference = "Stop"
$word = New-Object -ComObject Word.Application
$word.Visible = $false

try {
    $docPath = "C:\Users\S.RAJIT\.gemini\antigravity\scratch\lms\LearnSphere_LMS_PBL_Report.docx"
    Write-Host "Opening document: $docPath"
    $doc = $word.Documents.Open($docPath)
    
    # 1. Update the manual Table of Contents
    # We will search for "TABLE OF CONTENTS" and get the table that follows it.
    $selection = $word.Selection
    $selection.HomeKey(6) | Out-Null
    $find = $selection.Find
    $find.Text = "TABLE OF CONTENTS"
    $find.MatchCase = $true
    $find.MatchWholeWord = $true
    
    if ($find.Execute()) {
        Write-Host "Found 'TABLE OF CONTENTS' text."
        # Move down to find the table
        $selection.MoveDown(5, 1) | Out-Null # wdLine = 5
        if ($selection.Information(12)) { # wdWithInTable = 12
            Write-Host "Found the manual TOC table."
            $table = $selection.Tables.Item(1)
            
            # Now we iterate through the rows, read the title, find it in the doc, and get its new page number!
            for ($i = 2; $i -le $table.Rows.Count; $i++) {
                $cellTitle = $table.Cell($i, 2).Range.Text.Trim(" `r`n`a")
                if (-not [string]::IsNullOrWhiteSpace($cellTitle)) {
                    # Search for this title in the document
                    $searchRange = $doc.Content
                    $findTitle = $searchRange.Find
                    $findTitle.Text = $cellTitle
                    $findTitle.MatchWholeWord = $true
                    
                    # We want to skip the TOC match itself. So we start searching from after the TOC.
                    $searchRange.Start = $table.Range.End
                    
                    if ($findTitle.Execute()) {
                        # Get the page number!
                        $pageNum = $searchRange.Information(3) # wdActiveEndAdjustedPageNumber = 3
                        # Wait, Information(3) returns Arabic number always. 
                        # Is there a way to get the formatted page number string?
                        # No direct way in Word COM easily, but we can check the section's NumberStyle.
                        $secNum = $searchRange.Information(2) # wdActiveEndSectionNumber
                        $sec = $doc.Sections.Item($secNum)
                        $style = $sec.Footers.Item(1).PageNumbers.NumberStyle
                        
                        $pageStr = $pageNum.ToString()
                        if ($style -eq 2) { # Lowercase Roman
                            # Convert Arabic to Roman
                            $romans = @{1='i'; 2='ii'; 3='iii'; 4='iv'; 5='v'; 6='vi'; 7='vii'; 8='viii'; 9='ix'; 10='x'; 11='xi'; 12='xii'}
                            if ($romans.ContainsKey($pageNum)) {
                                $pageStr = $romans[$pageNum]
                            }
                        }
                        
                        # Write to the page number cell (column 3)
                        $table.Cell($i, 3).Range.Text = $pageStr
                        Write-Host "Updated '$cellTitle' to page $pageStr"
                    }
                }
            }
        }
    }
    
    # Do the same for LIST OF TABLES and LIST OF FIGURES
    $lists = @("LIST OF TABLES", "LIST OF FIGURES")
    foreach ($listName in $lists) {
        $selection.HomeKey(6) | Out-Null
        $find.Text = $listName
        if ($find.Execute()) {
            $selection.MoveDown(5, 1) | Out-Null
            if ($selection.Information(12)) {
                $table = $selection.Tables.Item(1)
                for ($i = 2; $i -le $table.Rows.Count; $i++) {
                    $cellTitle = $table.Cell($i, 2).Range.Text.Trim(" `r`n`a")
                    if (-not [string]::IsNullOrWhiteSpace($cellTitle)) {
                        $searchRange = $doc.Content
                        $findTitle = $searchRange.Find
                        $findTitle.Text = $cellTitle
                        $searchRange.Start = $table.Range.End
                        if ($findTitle.Execute()) {
                            $pageNum = $searchRange.Information(3)
                            $table.Cell($i, 3).Range.Text = $pageNum.ToString()
                        }
                    }
                }
                Write-Host "Updated $listName"
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
