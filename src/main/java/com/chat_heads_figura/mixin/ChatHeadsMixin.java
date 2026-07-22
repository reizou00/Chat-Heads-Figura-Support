/*
 * Copyright (c) 2026 reizou00
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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

@Mixin(ChatHeads.class)
public class ChatHeadsMixin {
    @Inject(
            method = "renderChatHead(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/multiplayer/PlayerInfo;FZ)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void renderFiguraHead(
            GuiGraphics guiGraphics, int x, int y, PlayerInfo owner, float opacity, boolean drawShadow, CallbackInfo ci) {

        Avatar avatar = AvatarManager.getAvatarForPlayer(owner.getProfile().getId());

        if (avatar == null) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.level != null ?
                mc.level.getPlayerByUUID(owner.getProfile().getId()) : null;
        if (player == null) return;

        // 表示するデータを取得
        // 描画出来なかったら元のメソッドの動作にする。
        if (!((ChatHeadsAvatar) avatar).chatHeads$renderPortrait(
                guiGraphics, x, y, 8, 16f, LivingEntityRenderer.isEntityUpsideDown(player), opacity)) return;

        // 元のメソッドはもういらへん！
        ci.cancel();
    }
}
