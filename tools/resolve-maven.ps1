function Resolve-MavenCommand {
    $command = Get-Command mvn -ErrorAction SilentlyContinue
    if ($command) { return $command.Source }

    $candidates = @(
        "C:\Maven\apache-maven-3.9.16\bin\mvn.cmd",
        "C:\Program Files\Apache\Maven\apache-maven-3.9.16\bin\mvn.cmd",
        "C:\apache-maven-3.9.16\bin\mvn.cmd"
    )

    foreach ($candidate in $candidates) {
        if (Test-Path $candidate) { return $candidate }
    }

    return $null
}
