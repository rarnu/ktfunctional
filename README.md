# ktfunctional

a functional library for kotlin

#### Using in Android

```
implementation "com.github.rarnu:ktfunctional:0.9.0"
```

#### Using in Kotlin Project

```
compile "com.github.rarnu:common:0.9.1"
```

#### Using in Ktor

```
compile "com.github.rarnu:ktor:0.2.0"
```

- - -

#### Try this

```kotlin
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

- - -

#### Usage for Ktor

**access database**

```hocon
ktor {
    ... ...
    database {
        driver = "com.mysql.cj.jdbc.Driver"
        url = "jdbc:mysql://localhost:3306/SampleDB?useUnicode=true&characterEncoding=UTF-8"
        user = "root"
        password = "root"
    }
}
```

You may access ```application.conn``` for a database connection after that.

**session & plugins**

```kotlin
installPlugin<MySession>(sessionIdentifier = "MySession", headers = mapOf("X-Engine" to "Ktor")) { 
    ... ...
}
```

**inline your customized session:**

```kotlin
inline val PipelineContext<*, ApplicationCall>.mySession: MySession
    get() = session {
        MySession(UUID.randomUUID().toString())
    }
```

**save a file from POST**

```kotlin
val file = call.receiveMultipart().file("file")
"${call.config("ktor.image.path")}/$uuid".asFileMkdirs()
file.save(File("${call.config("ktor.image.path")}/$uuid/upload.jpg"))
```

Inside the library there are many other ```ktor``` features.
