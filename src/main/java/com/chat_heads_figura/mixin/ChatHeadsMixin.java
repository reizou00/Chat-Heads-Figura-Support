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
            method = "renderChatHead",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void renderFiguraHead(
            GuiGraphics guiGraphics, int x, int y, PlayerInfo owner, float opacity, CallbackInfo ci) {
        Avatar avatar = AvatarManager.getAvatarForPlayer(
                owner.getProfile().getId()
        );

        if (avatar == null)
            return;

        Player player = Minecraft.getInstance()
                .level
                .getPlayerByUUID(owner.getProfile().getId());

        boolean upsideDown =
                player != null &&
                        LivingEntityRenderer.isEntityUpsideDown(player);

        ((ChatHeadsAvatar) avatar).chatHeads$renderPortrait(
                guiGraphics,
                x,
                y,
                8,
                16f,
                upsideDown,
                opacity
        );
        {
            ci.cancel();
        }
    }
}
