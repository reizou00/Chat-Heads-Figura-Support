package com.chat_heads_figura.client.mixin;

import com.chat_heads_figura.client.interfaces.ChatHeadsAvatar;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.mixin.gui.GuiGraphicsAccessor;
import org.figuramc.figura.model.rendering.AvatarRenderer;
import org.figuramc.figura.model.rendering.PartFilterScheme;
import org.figuramc.figura.utils.ui.UIHelper;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Avatar.class)
public abstract class AvatarMixin implements ChatHeadsAvatar {

    @Shadow
    public AvatarRenderer renderer;

    @Shadow
    public boolean loaded;

    @Unique
    Avatar avatar = (Avatar)(Object)this;

    @Unique
    @Override
    public boolean chatHeads$renderPortrait(GuiGraphics gui, int x, int y, int size, float modelScale, boolean upsideDown, float alpha) {
        if (Configs.AVATAR_PORTRAIT.value && this.renderer != null && this.loaded) {
            PoseStack pose = gui.pose();
            pose.pushPose();
            pose.translate(x, y, (double)0.0F);
            pose.scale(modelScale, modelScale * (float)(upsideDown ? 1 : -1), modelScale);
            pose.mulPose(Axis.XP.rotationDegrees(180.0F));
            Vector3f pos = pose.last().pose().transformPosition(new Vector3f());
            int x1 = (int)pos.x;
            int y1 = (int)pos.y;
            int x2 = (int)pos.x + size;
            int y2 = (int)pos.y + size;
            gui.pose().pushPose();
            gui.pose().setIdentity();
            gui.enableScissor(x1, y1, x2, y2);
            gui.pose().popPose();
            UIHelper.paperdoll = true;
            UIHelper.dollScale = 16.0F;
            pose.translate(0.25F, upsideDown ? (double)0.0F : (double)0.5F, (double)0.0F);
            Lighting.setupForFlatItems();
            MultiBufferSource.BufferSource buffer = ((GuiGraphicsAccessor)gui).getBufferSource();
            int light = 15728880;
            this.renderer.allowPivotParts = false;
            this.renderer.setupRenderer(PartFilterScheme.PORTRAIT, buffer, pose, 1.0F, light, alpha, OverlayTexture.NO_OVERLAY, false, false);
            int comp = this.renderer.renderSpecialParts();
            boolean ret = comp > 0 || avatar.headRender(pose, buffer, light, false);
            buffer.endBatch();
            pose.popPose();
            gui.disableScissor();
            UIHelper.paperdoll = false;
            this.renderer.allowPivotParts = true;
            return ret;
        } else {
            return false;
        }
    }
}
