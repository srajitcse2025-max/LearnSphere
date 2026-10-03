$ErrorActionPreference = "Stop"
$word = New-Object -ComObject Word.Application
$word.Visible = $false

try {
    $docPath = "C:\Users\S.RAJIT\.gemini\antigravity\scratch\lms\LearnSphere_LMS_PBL_Report.docx"
    Write-Host "Opening document: $docPath"
    $doc = $word.Documents.Open($docPath)
    
    $selection = $word.Selection
    $selection.HomeKey(6) | Out-Null # wdStory = 6
    
    # Search for "CHAPTER 1"
    $find = $selection.Find
    $find.Text = "CHAPTER 1"
    $find.MatchCase = $true
    $find.MatchWholeWord = $true
    $found = $find.Execute()
    
    if ($found) {
        Write-Host "Found 'CHAPTER 1'."
        $selection.Collapse(1) # wdCollapseStart = 1
        
        # Check if we need to replace a page break with a section break
        $selection.MoveLeft(1, 1, 1) | Out-Null # wdCharacter = 1, Count = 1, wdExtend = 1
        if ($selection.Text -eq "`f" -or $selection.Text -eq "^m") { 
            Write-Host "Replacing page break with section break."
            $selection.Delete()
        } else {
            $selection.Collapse(0) | Out-Null # wdCollapseEnd
        }
        
        # Insert Section Break Next Page
        $selection.InsertBreak(2) | Out-Null # wdSectionBreakNextPage = 2
        
        # Determine the section index of CHAPTER 1 (which is now after the break)
        $selection.MoveRight(1, 1) | Out-Null # Move into the new section
        $ch1SecNum = $selection.Information(2) # wdActiveEndSectionNumber = 2
        Write-Host "CHAPTER 1 is in Section $ch1SecNum"
        
        # Format Front Matter (Sections before CHAPTER 1)
        for ($i = 1; $i -lt $ch1SecNum; $i++) {
            $sec = $doc.Sections.Item($i)
            $footer = $sec.Footers.Item(1) # wdHeaderFooterPrimary = 1
            $footer.PageNumbers.NumberStyle = 2 # wdPageNumberStyleLowercaseRoman = 2
            if ($i -eq 1) {
                $footer.PageNumbers.RestartNumberingAtSection = $true
                $footer.PageNumbers.StartingNumber = 1
            }
            # Ensure page numbers are visible (bottom center)
            $footer.PageNumbers.Add(1, $true) | Out-Null # wdAlignPageNumberCenter = 1, FirstPage = true
        }
        
        # Format Main Content (Section containing CHAPTER 1 and onwards)
        for ($i = $ch1SecNum; $i -le $doc.Sections.Count; $i++) {
            $sec = $doc.Sections.Item($i)
            $footer = $sec.Footers.Item(1)
            $footer.LinkToPrevious = $false
            $footer.PageNumbers.NumberStyle = 0 # wdPageNumberStyleArabic = 0
            if ($i -eq $ch1SecNum) {
                $footer.PageNumbers.RestartNumberingAtSection = $true
                $footer.PageNumbers.StartingNumber = 1
            }
            $footer.PageNumbers.Add(1, $true) | Out-Null
        }
        
        # Update Table of Contents
        if ($doc.TablesOfContents.Count -gt 0) {
            Write-Host "Updating Table of Contents..."
            $doc.TablesOfContents.Item(1).Update()
        } else {
            Write-Host "No Table of Contents found to update."
        }
        
        $doc.Save()
        Write-Host "Document saved successfully."
    } else {
        Write-Host "Error: 'CHAPTER 1' not found in the document."
    }
} catch {
    Write-Host "An error occurred: $_"
} finally {
    if ($doc) { 
        $doc.Close(-1) # wdDoNotSaveChanges = 0, wdSaveChanges = -1
        [System.Runtime.Interopservices.Marshal]::ReleaseComObject($doc) | Out-Null
    }
    if ($word) { 
        $word.Quit() 
        [System.Runtime.Interopservices.Marshal]::ReleaseComObject($word) | Out-Null
    }
    [System.GC]::Collect()
    [System.GC]::WaitForPendingFinalizers()
}
