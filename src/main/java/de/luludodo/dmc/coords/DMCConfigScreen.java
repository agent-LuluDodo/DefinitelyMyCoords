package de.luludodo.dmc.coords;

import de.luludodo.dmc.config.ConfigAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;

@Environment(value=EnvType.CLIENT)
public class DMCConfigScreen extends Screen {
    private final GameOptions settings;

    private CyclingButtonWidget<Mode> mode;
    private SimpleOption<Boolean> randomRotations;
    private TextFieldWidget offsetX;
    private TextFieldWidget offsetY;
    private TextFieldWidget offsetZ;

    private final Element[] elements = new Element[9];
    private boolean infoMode;

    private int relativeX = 0;
    private int relativeY = 0;
    private int relativeZ = 0;
    private int absoluteX = 0;
    private int absoluteY = 0;
    private int absoluteZ = 0;

    private int oldX = 0;
    private int oldY = 0;
    private int oldZ = 0;
    private Mode oldMode = null;
    private boolean oldRandomRotations = true;
    private boolean oldDebugEnabled = false;

    public DMCConfigScreen(Screen parent, GameOptions gameOptions) {
        super(Text.translatable("options.dmc.title"));
        this.settings = gameOptions;
    }

    protected void init() {
        oldX = ConfigAPI.getOffsetX();
        oldY = ConfigAPI.getOffsetY();
        oldZ = ConfigAPI.getOffsetZ();
        oldMode = ConfigAPI.getMode();
        oldRandomRotations = ConfigAPI.getObscureRotations();
        oldDebugEnabled = client.options.debugEnabled;
        client.options.debugEnabled = true;
        randomRotations = SimpleOption.ofBoolean("options.dmc.random-rotations", ConfigAPI.getObscureRotations(), value -> {
            ConfigAPI.setObscureRotations(value);
            client.worldRenderer.reload();
        });
        elements[0] = addDrawableChild(mode = CyclingButtonWidget.builder(
                (Mode mode) -> Text.translatable(mode.getTranslationKey())
        ).values(
                Mode.values()
        ).build(
                width / 2 - 100,
                height / 6 + 8,
                176,
                20,
                Text.translatable("options.dmc.offset-mode"),
                this::onModeChange
        ));
        offsetX = new TextFieldWidget(textRenderer, width / 2 - 76, height / 6 + 32, 176, 20, offsetX, Text.translatable("options.dmc.offset-x"));
        offsetY = new TextFieldWidget(textRenderer, width / 2 - 76, height / 6 + 56, 176, 20, offsetY, Text.translatable("options.dmc.offset-y"));
        offsetZ = new TextFieldWidget(textRenderer, width / 2 - 76, height / 6 + 80, 176, 20, offsetZ, Text.translatable("options.dmc.offset-z"));
        offsetX.setChangedListener(offset -> {
            try {
                if(mode.getValue() == Mode.ABSOLUTE) {
                    ConfigAPI.setOffsetX(Integer.parseInt(offset.replaceAll("^$", "0")));
                } else {
                    ConfigAPI.setOffsetX(Integer.parseInt(offset.replaceAll("^$", "0")) - client.cameraEntity.getBlockX());
                }
            } catch (Exception ignored) {}
        });
        offsetY.setChangedListener(offset -> {
            try {
                if(mode.getValue() == Mode.ABSOLUTE) {
                    ConfigAPI.setOffsetY(Integer.parseInt(offset.replaceAll("^$", "0")));
                } else {
                    ConfigAPI.setOffsetY(Integer.parseInt(offset.replaceAll("^$", "0")) - client.cameraEntity.getBlockY());
                }
            } catch (Exception ignored) {}
        });
        offsetZ.setChangedListener(offset -> {
            try {
                if(mode.getValue() == Mode.ABSOLUTE) {
                    ConfigAPI.setOffsetZ(Integer.parseInt(offset.replaceAll("^$", "0")));
                } else {
                    ConfigAPI.setOffsetZ(Integer.parseInt(offset.replaceAll("^$", "0")) - client.cameraEntity.getBlockZ());
                }
            } catch (Exception ignored) {}
        });
        elements[1] = addDrawableChild(randomRotations.createWidget(settings, width / 2 - 100, height / 6 - 16, 176));
        elements[6] = addDrawableChild(ButtonWidget.builder(Text.of("⟳"), button -> {
            ConfigAPI.setOffsetRotations((int) Math.round(Math.random() * 10000));
            if (randomRotations.getValue()) {
                client.worldRenderer.reload();
            }
        }).dimensions(width / 2 + 80, height / 6 - 16, 20, 20).build());
        elements[5] = addDrawableChild(ButtonWidget.builder(Text.of("i"), button -> infoMode = !infoMode).dimensions(width / 2 + 80, height / 6 + 8, 20, 20).build());
        Mode mode = ConfigAPI.getMode();
        if (mode != Mode.CUSTOM) {
            elements[2] = addDrawableChild(offsetX);
            elements[3] = addDrawableChild(offsetY);
            elements[4] = addDrawableChild(offsetZ);
            offsetX.setText((ConfigAPI.getOffsetX() + (mode == Mode.RELATIVE?client.cameraEntity.getBlockX(): 0) + "").replaceAll("\\.0$", ""));
            offsetY.setText((ConfigAPI.getOffsetY() + (mode == Mode.RELATIVE?client.cameraEntity.getBlockY(): 0) + "").replaceAll("\\.0$", ""));
            offsetZ.setText((ConfigAPI.getOffsetZ() + (mode == Mode.RELATIVE?client.cameraEntity.getBlockZ(): 0) + "").replaceAll("\\.0$", ""));
        }
        elements[8] = addDrawableChild(ButtonWidget.builder(Text.translatable("options.dmc.cancel"), button -> cancel()).dimensions(width / 2 - 100, height / 6 + 168, 200, 20).build());
        elements[7] = addDrawableChild(ButtonWidget.builder(Text.translatable("options.dmc.save"), button -> close()).dimensions(width / 2 - 100, height / 6 + 144, 200, 20).build());
    }

    private void onModeChange(CyclingButtonWidget<Mode> widget, Mode mode) {
        if (mode == Mode.CUSTOM) {
            if (this.children().contains(offsetX)) {
                remove(offsetX);
                remove(offsetY);
                remove(offsetZ);
            }
            ConfigAPI.setMode(Mode.CUSTOM);
            return;
        } else {
            if (!this.children().contains(offsetX)) {
                elements[2] = addDrawableChild(offsetX);
                elements[3] = addDrawableChild(offsetY);
                elements[4] = addDrawableChild(offsetZ);
            }
        }
        if (mode == Mode.RELATIVE) {
            offsetX.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetX() + client.cameraEntity.getBlockX()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
            offsetY.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetY() + client.cameraEntity.getBlockY()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
            offsetZ.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetZ() + client.cameraEntity.getBlockZ()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
        } else  {
            offsetX.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetX()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
            offsetY.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetY()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
            offsetZ.setText(
                    String.valueOf(
                            ConfigAPI.getOffsetZ()
                    ).replaceAll(
                            "\\.0$",
                            ""
                    )
            );
        }
        ConfigAPI.setMode(mode);
    }

    public void close() {
        client.options.debugEnabled = oldDebugEnabled;
        client.worldRenderer.reload();
        super.close();
    }

    public void cancel() {
        ConfigAPI.setOffsetX(oldX);
        ConfigAPI.setOffsetY(oldY);
        ConfigAPI.setOffsetZ(oldZ);
        ConfigAPI.setMode(oldMode);
        ConfigAPI.setObscureRotations(oldRandomRotations);
        client.worldRenderer.reload();
        close();
    }

    private static int validNumber(String value) {
        if(value.length() == 0) {
            return 0;
        }
        try {
            Float.parseFloat(value);
        } catch (Exception e) {
            return 2;
        }
        return 1;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        drawContext.drawCenteredTextWithShadow(textRenderer, title, width / 2, 5, 0xFFFFFF);
        if (mode.getValue() != Mode.CUSTOM) {
            drawCenteredTextWithShadowOfOffsetValue(offsetX, "X", 38, drawContext);
            drawCenteredTextWithShadowOfOffsetValue(offsetY, "Y", 62, drawContext);
            drawCenteredTextWithShadowOfOffsetValue(offsetZ, "Z", 86, drawContext);
        }
        if (infoMode) infoMode(drawContext, mouseX, mouseY, delta);
        super.render(drawContext, mouseX, mouseY, delta);
    }

    private void infoMode(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        Optional<Element> optionalHoveredElement= this.hoveredElement(mouseX, mouseY);
        if (optionalHoveredElement.isPresent()) {
            Element hoveredElement = optionalHoveredElement.get();
            int index = -1;
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].equals(hoveredElement)) index = i;
            }
            switch (index) {
                case 0 -> renderTooltip(drawContext, "mode-" + mode.getValue().toString(), mouseX, mouseY);
                case 1 -> renderTooltip(drawContext, "obscure-rotations", mouseX, mouseY);
                case 2 -> renderTooltip(drawContext, "offset-x", width / 2 + 95, mouseY);
                case 3 -> renderTooltip(drawContext, "offset-y", width / 2 + 95, mouseY);
                case 4 -> renderTooltip(drawContext, "offset-z", width / 2 + 95, mouseY);
                case 5 -> renderTooltip(drawContext, "info-mode", mouseX, mouseY);
                case 6 -> renderTooltip(drawContext, "redo-rotations", mouseX, mouseY);
                case 7 -> renderTooltip(drawContext, "save", mouseX, mouseY);
                case 8 -> renderTooltip(drawContext, "cancel", mouseX, mouseY);
            }
        }
    }

    private void renderTooltip(DrawContext drawContext, String name, int x, int y) {
        drawContext.drawTooltip(textRenderer, Text.translatable("tooltip.dmc." + name), x, y);
    }

    private void drawCenteredTextWithShadowOfOffsetValue(TextFieldWidget offset, String name, int heightOffset, DrawContext drawContext) {
        int V = validNumber(offset.getText());
        int color = V == 0? 0xFFFFFF: V == 1? 0x00FF00: 0xFF0000;
        drawContext.drawCenteredTextWithShadow(textRenderer, name, width / 2 - 88, height / 6 + heightOffset, color);
    }
}