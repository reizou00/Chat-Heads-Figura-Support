/*
 * Copyright (c) 2026 reizou00
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.chat_heads_figura.interfaces;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Unique;

public interface ChatHeadsAvatar {
    @Unique
    boolean chatHeads$renderPortrait(GuiGraphics gui, int x, int y, int size, float modelScale, boolean upsideDown, float alpha);
}
