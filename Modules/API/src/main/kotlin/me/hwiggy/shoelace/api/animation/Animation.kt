package me.hwiggy.shoelace.api.animation

abstract class Animation<Canvas> : Iterator<Animation.Frame<Canvas>> {

    abstract class Frame<Canvas> {
        abstract val configurator: () -> Manipulator<Canvas>

        open fun repeat() = false

        open fun render(canvas: Canvas) = configurator().applyTo(canvas)
    }

    interface Manipulator<Canvas> {
        fun applyTo(canvas: Canvas)
    }
}