; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Get Series"
#define MyAppVersion "1.0"
#define MyAppPublisher "getSeries.com.br"
#define MyAppExeName "GsServer.jar"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{4D3636E8-24D0-44AE-9C9D-14C766CE1271}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={pf}\GetSeriesServer
DefaultGroupName={#MyAppName}
OutputDir=Versao
OutputBaseFilename=GsServer
Compression=lzma2/ultra64
SolidCompression=yes


[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "dist\GsServer.jar";            DestDir: "{app}";       Flags: ignoreversion
Source: "libs\*";                   DestDir: "{app}\lib";   Flags: ignoreversion
Source: "icons\*";                 DestDir: "{app}\icons"; Flags: ignoreversion
Source: "configs.properties";      DestDir: "{app}";       Flags: onlyifdoesntexist


[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\icons\ic_launcher32x32.ico"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\icons\ic_launcher32x32.ico";Tasks: desktopicon

;[Run]
;Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

