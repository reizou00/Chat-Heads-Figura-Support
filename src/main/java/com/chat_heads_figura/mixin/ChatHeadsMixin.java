package com.chat_heads_figura.mixin;

import com.chat_heads_figura.interfaces.ChatHeadsAvatar;
import dzwdz.chat_heads.ChatHeads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Copyright (c) 2026 reizou00
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

@Mixin(ChatHeads.class)
public class ChatHeadsMixin {
    @Inject(
            method = "renderChatHead",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void renderFiguraHead(
            GuiGraphics guiGraphics, int x, int y, PlayerInfo owner, float opacity, CallbackInfo ci) {

        Avatar avatar = AvatarManager.getAvatarForPlayer(owner.getProfile().getId());

        if (avatar == null)
            return;

        Player player = Minecraft.getInstance()
                .level != null ? Minecraft.getInstance()
                                 .level
                                 .getPlayerByUUID(owner.getProfile().getId()) : null;

        boolean upsideDown =
                player != null &&
                        LivingEntityRenderer.isEntityUpsideDown(player);

        // 表示するデータを取得
        boolean head = ((ChatHeadsAvatar) avatar).chatHeads$renderPortrait(
                guiGraphics,
                x,
                y,
                8,
                16f,
                upsideDown,
                opacity
        );

        // 描画出来なかったら元のメソッドの動作にする。
        if (!head) return;

        // 元のメソッドはもういらへん！
        ci.cancel();
    }
}
