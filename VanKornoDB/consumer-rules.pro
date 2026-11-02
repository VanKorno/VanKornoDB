# Keep all classes implementing DbEntity and their primary constructors
-keep class * implements com.vankorno.vankornodb.dbManagement.data.BaseEntity {
    <init>(...);
}

# Keep all fields and methods (needed for defaultInstanceValueOf and reflection)
-keepclassmembers class * implements com.vankorno.vankornodb.dbManagement.data.BaseEntity {
    <fields>;
    <methods>;
}

# Keep constructor parameter names (needed for reflection-based mapping)
-keepattributes RuntimeVisibleParameterAnnotations,ParameterNames
