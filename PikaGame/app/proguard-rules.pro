# Keep classes in this package intact (prevent obfuscation)
-keep class dev.pikagame.** { *; }

# Keep specific class or method intact (prevent obfuscation)
-keep class dev.pikagame.MainActivity { *; }
-keep class dev.pikagame.Mechanics { *; }
-keep class dev.pikagame.CustomManager { *; }
-keep class dev.pikagame.Policy { *; }
-keep class dev.pikagame.GlobalWebSetting { *; }