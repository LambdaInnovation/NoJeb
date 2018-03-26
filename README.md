# NoJeb

A project that aims at optimizing workspace Minecraft startup speed, to achieve a faster edit-compile-test cycle.

It is build in frustration that Minecraft startup takes up to ~40secs in an up-to-date computer, which greatly impairs modder's productivity.

It plans to do the following:

* Create a complete time report for each Minecraft loading process
* Selectively disable features to startup loading
    * Disable unused blocks/items
    * Disable some network services (e.g. Minecraft Realms) that blocks world loading

