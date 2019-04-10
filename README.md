# ktfunctional
a functional library for kotlin(android/jvm)

#### Using in Android

```
implementation "com.github.rarnu:ktfunctional:0.8.5"
```

#### Using in Kotlin Project

```
implementation "com.github.rarnu:common:0.8.0"
```

#### Try this

```
fileIO {
    src = "/sdcard/a.txt"
    isDestText = true
    result { status, text, errMsg ->
        if (status) {
            Log.e(tag, "$text")
        } else {
            Log.e(tag, "$errMsg")
        }
    }
}

fileIO {
    src = "/sdcard/a.txt"
    dest = "/sdcard/b.txt"
    result { status, text, errMsg ->
        if (status) {
            Log.e(tag, "ok")
        } else {
            Log.e(tag, "$errMsg")
        }
    }
}

runCommand {
    commands.add("ls")
    commands.add("/sdcard/")
    result { output, error ->
        Log.e(tag, "output => $output, error => $error")
    }
}

UI.init(ctx)
val padding = 8.dip2px()

```
