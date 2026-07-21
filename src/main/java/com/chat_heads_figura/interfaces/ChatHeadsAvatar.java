package com.chat_heads_figura.interfaces;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Unique;

public interface ChatHeadsAvatar {
    @Unique
    boolean chatHeads$renderPortrait(GuiGraphics gui, int x, int y, int size, float modelScale, boolean upsideDown, float alpha);
}
