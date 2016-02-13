# PrefsOven
Oven for Prefs: Managing SharedPreferences without APT

**NOTE: Now depending on AndroidAnnotations classes**

## Build with

```groovy
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

```groovy
	dependencies {
		compile 'com.github.k24:prefsoven:0.2.0'
	}
```

## Feature
- Simple Oven
 - Preheat Oven: Apply values to Prefs
 - Bake object: Get values from Prefs
 - Control: Clear all values
 - Customize type: Use customized types
- Store Oven (Experimental)
 - Simple KVS: PID-*Key-Value
 - Preheat Oven: Apply values to Prefs with/without PID
 - Bake object: Get values from Prefs with PID
 - Control: List pids/keys/elements, Clear all values
 - Customize type: Use customized types

## Usage of Oven

```java
// Object to bake
public class Bread {
	public String str;
	public int number;
}
```

```java
// Oven
public interface Oven extends PrefsOven {
	StringPref str;
	IntPref number;
}
```

### Preheat Oven
```java
Bread bread = new Bread();
bread.str = "default";
bread.number = 1;
Prefs.oven(Oven.class).getControlPanel().preheat(bread);
//context.getSharedPreferences("Oven", Context.MODE_PRIVATE).edit()
//	.putString("str", "default")
//	.putInt("number", 1)
//	.apply();
```

### Bake object
```java
Bread bread = new Bread();
Prefs.oven(Oven.class).bake(bread);
//SharedPreferences prefs = context.getSharedPreferences("Oven", Context.MODE_PRIVATE);
//bread.str = prefs.getString("str");
//bread.number = prefs.getInt("number", 0);
```
```java
// Easier way
Bread bread = Prefs.oven(Oven.class).cook(Bread.class);
```

### Customize type

Create customized prefs and their factory like [CustomFieldFactory](prefsoven/src/test/java/com/github/k24/prefsoven/custom/CustomFieldFactory.java), 
[DoublePref](prefsoven/src/test/java/com/github/k24/prefsoven/custom/DoublePref.java).

```java
Prefs.install(application, PrefsConfig.newBuilder()
	.fieldFactory(new CustomFieldFactory())
	.build());
```

## Usage of Store
```java
// Object to bake
public class Bread {
	public String str;
	public int number;
}
```

```java
// Oven
public interface StoreOven extends PrefsStoreOven {
	StringElement str;
	IntElement number;
}
```

### Preheat Oven
```java
// Create new PID from default values
Pid pid = Prefs.store(StoreOven.class).getControlPanel().preheat();
```
```java
// Create new PID from specified values
Bread bread = new Bread();
bread.str = "default";
bread.number = 1;
Pid pid = Prefs.store(StoreOven.class).getControlPanel().preheat(bread);
```
```java
// Update values to the first PID
Pid pid = Prefs.store(StoreOven.class).getControlPanel().pids().get(0);
Bread bread = new Bread();
bread.str = "other";
bread.number = 100;
Prefs.store(StoreOven.class).getControlPanel().preheat(pid, bread);
```

### Bake object
```java
Bread bread = new Bread();
Prefs.oven(Oven.class).bake(bread);
//SharedPreferences prefs = context.getSharedPreferences("Oven", Context.MODE_PRIVATE);
//bread.str = prefs.getString("str");
//bread.number = prefs.getInt("number", 0);
```

### Customize type

Create customized elements and their factory like [CustomElementFactory](prefsoven/src/test/java/com/github/k24/prefsoven/custom/CustomElementFactory.java), 
[DoubleElement](prefsoven/src/test/java/com/github/k24/prefsoven/custom/DoubleElement.java).

```java
Prefs.install(application, PrefsConfig.newBuilder()
	.elementFactory(new CustomElementFactory())
	.build());
```
