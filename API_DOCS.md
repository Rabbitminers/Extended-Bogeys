# Using The Bogey API

---

Since 0.5.1 Extended Bogey's (EB) API for creating custom bogeys has been implemented into Create. This means that you will no longer need to depend on EB to create your own styles although doing so will provide better handling for bogey painting, rotation etc.

## Creating new bogey styles

---

### Renderers

For your new bogey style you'll need a renderer (singular) both contraption rendering and in-world rendering is handled for you by the API.

To create a new Bogey Renderer create a class that extends BogeyRenderer like so

```java
public static class SmallDoubleAxleBogeyRender extends BogeyRenderer
```

There are three required methods `render`, `getSize` and `initialiseContraptionModelData`

#### `getSize`
 This is the size your renderer is for, i.e Small, Large or any other custom sizes you create

#### `initialiseContraptionModelData`
 Within a contraption model data for custom bogey styles is instaned. This is where you can register instances of your model data using `BogeyRenderer#createModelInstances` like so

```java 
this.createModelInstances(materialManager, model, count);
```

Where `materialManager` is the manager passed in `initialiseContraptionModelData`, where `model` is a type of model to create data for (either a PartialModel or BlockState) and count is the number of instances you need.

If you only need to create a single instance you can skip the count arguement like so

```java 
this.createModelInstances(materialManager, model);
```

Or pass in as many models as you like to create singular instances of

```java 
this.createModelInstances(materialManager, model_one, model_two, model_three, ...);
```

Please note - this isnt some magical method or the only way that works, feel free to adapt to your own use cases.

#### `render`
This is where the magic happens! You can now retrieve the model data you initialised, or for in-world rendering be served the relevant data, by calling `BogeyRenderer#getTransform` like so 

```java
boolean inContraption = vb == null;

// This generic Transform object allows for both in-world and in-contraption data to be handled in the same manner
Transform<?>[] transforms = this.getTransform(model, ms, inContraption, count);

// If you only need a single transform you can skip the count arguement
Transform<?> transform = this.getTransform(model, ms, inContraption);
```
You can now manipulate this transform as you please say setting scale, rotation, etc, for example

```java
// For a single model, such as a pair of wheels
Transform<?> bogeyWheels = getTransform(LARGE_BOGEY_WHEELS, ms, inContraption)
                .translate(0, 1, 0) // Move the wheels up one block
                .rotateX(wheelAngle); // Rotate them to the desired position

// Or if you have multiple transforms, such as these shafts
Transform<?>[] secondaryShafts = getTransform(AllBlocks.SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X), ms, inContraption, 2);

for (int i : Iterate.zeroAndOne) {
        Transform<?> secondShaft = secondaryShafts[i];
        secondShaft.translate(-.5f, .25f, .5f + i * -2)
                        .centre()
                        .rotateX(wheelAngle)
                        .unCentre();
}
```

Once you are finished modifying the model you need to "finalize" it like so, this will handle render case specific bits and bobs such as setting lighting for an in-world model.

```java
// For a single model
BogeyRenderer.finalize(transform, ms, light, vb);
```

If you are using multiple models it would be reccomended to finalize them as you manipulate each one, for example lets implement that for the shafts shown above

```java
// Collect transforms...

for (int i : Iterate.zeroAndOne) {
        Transform<?> secondShaft = secondaryShafts[i];
        secondShaft.translate(-.5f, .25f, .5f + i * -2)
                        .centre()
                        .rotateX(wheelAngle)
                        .unCentre();
        finalize(secondShaft, ms, light, vb); // <-- finalize each shaft
}
```

And your done! Lighting, removal, emptying transforms etc is all handled automatically by the API

### Registering Bogey Styles

To make this explanation easier to follow heres the example style classes we'll be using with a common and small renderer. Note you do not need to have a common renderer, it is only here as an example although you need a renderer for **at least one** size.

```java
public class ExampleBogeyRenderer {
                
    public static class CommonExampleBogeyRender extends BogeyRenderer.CommonRenderer {
        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light,
                           VertexConsumer vb, boolean inContraption) {
                /* Common rendering stuff... */
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
                /* Initalise contraption data... */
        }
    }

    public static class SmallExampleBogeyRender extends BogeyRenderer {
        @Override
        public void render(boolean forwards, BogeyPaintColour color, float wheelAngle, PoseStack ms, int light,
                           VertexConsumer vb, boolean inContraption) {
                /* Rendering stuff... */
        }

        @Override
        public BogeySizes.BogeySize getSize() {
            return BogeySizes.SMALL;
        }

        @Override
        public void initialiseContraptionModelData(MaterialManager materialManager) {
                /* Initalise contraption data... */
        }
    }
}
```

Although there is no strict requirement to do so, it is best practice to register your bogey styles during mod initialisation (at the same point as you would register blocks, entities, etc)

To build and register your own style call `AllBogeyStyles#create` taking two resource locations and which returns a `BogeyStyleBuilder`.

The first resource location should be the name of your style, within your mod id to prevent conflicts or styles being overwritten due to duplicate names.

```java
public static final ResourceLocation styleName = new ResourceLocation(ExampleMod.MOD_ID, "example_style")
```

The second is the cycle group you wish to use. This helps players navigate through bogey styles by bundling sets of them into groups. Unless you are trying to add into creates original set of bogeys such as a new size for them we reccomend create a new cycle group for your mod. 

```java
public static final ResourceLocation cycleGroup = new ResourceLocation(ExampleMod.MOD_ID, "example_cylce_group")
```

Now you can build and register your own style like so

```java
public static final BogeyStyle EXAMPLE_STYLE = AllBogeyStyles.create(styleName, cycleGroup)
        .commonRenderer(() -> ExampleBogeyRenderer.CommonBogeyRenderer::new)
        .size(BogeySizes.SMALL, () -> ExampleBogeyRenderer.SmallExampleBogeyRenderer::new, AllBlocks.SMALL_BOGEY)
        .build();
```

This is the absolute bare bones for registering a style with just a size and common renderer. If you open your game you should now be able to access your new style! However you will also likely want to check out some of the other bits and bobs you can add to you style as seen here:

https://github.com/Creators-of-Create/Create/blob/mc1.19/dev/src/main/java/com/simibubi/create/AllBogeyStyles.java#L57