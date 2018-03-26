# NoJeb

A project that aims at optimizing workspace Minecraft startup speed, to achieve a faster edit-compile-test cycle.

It is build in frustration that Minecraft startup takes up to ~40secs in an up-to-date computer, which greatly impairs modder's productivity.

The mod selectively disables certain feature(s) that are not useful during development, including:

* Sounds
* Item/Block Models
* Yggdrasil Authentication Service

# Effect

| Vanilla MC | NoJeb, all flags on |
| --- | --- |
| 28.0s | 18.0s |

* Platform: i7-7700HQ 2.80GHz / 8G RAM / GTX1050i / Windows 10
* IDE: Intellij IDEA
* Tested on a A small mod written in Kotlin
* Time span = Time of last log entry - Time of first log entry
* Startup time is measured 3 times and taken average

# Usage

Clone the project and build the jar using:

```
gradlew build
```

And then add the `deobf` version of jar into your workspace.

Add the following VM Arguments to enable the mod's behaviours:

* `-Dnoauth`: Disable Yggdrasil authentication service
    * Saves time of world loading, as it keeps sending failed HTTP requests.
* `-Dnosound`: Disable loading of all sounds
    * Saves about 2s
* `-Dnomodel`: Disable loading of all models 
    * useful when not developing items/blocks
    * Saves about 10s

License
---

MIT