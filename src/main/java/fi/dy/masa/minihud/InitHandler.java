package fi.dy.masa.minihud;

import malilib.config.JsonModConfig;
import malilib.config.JsonModConfig.ConfigDataUpdater;
import malilib.config.util.ConfigUpdateUtils.KeyBindSettingsResetter;
import malilib.event.InitializationHandler;
import malilib.gui.config.ConfigSearchInfo;
import malilib.registry.Registry;
import fi.dy.masa.minihud.config.ConfigCallbacks;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoLineToggle;
import fi.dy.masa.minihud.config.RendererToggle;
import fi.dy.masa.minihud.config.StructureToggle;
import fi.dy.masa.minihud.event.ClientTickHandler;
import fi.dy.masa.minihud.event.ClientWorldChangeHandler;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.feature.Actions;
import fi.dy.masa.minihud.gui.ConfigScreen;
import fi.dy.masa.minihud.gui.widget.InfoLineConfigWidget;
import fi.dy.masa.minihud.gui.widget.RendererToggleConfigWidget;
import fi.dy.masa.minihud.gui.widget.StructureToggleConfigWidget;
import fi.dy.masa.minihud.gui.widget.info.RendererToggleConfigStatusWidget;
import fi.dy.masa.minihud.gui.widget.info.StructureRendererConfigStatusWidget;
import fi.dy.masa.minihud.input.MiniHudHotkeyProvider;
import fi.dy.masa.minihud.network.servux.ServuxInfoSubDataPacketHandler;
import fi.dy.masa.minihud.network.servux.ServuxInfoSubRegistrationPacketHandler;

public class InitHandler implements InitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        // Reset all KeyBindSettings when updating to the first post-malilib-refactor version
        ConfigDataUpdater updater = new KeyBindSettingsResetter(MiniHudHotkeyProvider.INSTANCE::getAllHotkeys, 0);
        Registry.CONFIG_MANAGER.registerConfigHandler(JsonModConfig.createJsonModConfig(Reference.MOD_INFO, Configs.CURRENT_VERSION, Configs.CATEGORIES, updater));

        Registry.CONFIG_SCREEN.registerConfigScreenFactory(Reference.MOD_INFO, ConfigScreen::create);
        Registry.CONFIG_TAB.registerConfigTabProvider(Reference.MOD_INFO, ConfigScreen::getConfigTabs);

        Registry.CONFIG_WIDGET.registerConfigWidgetFactory(InfoLineToggle.class, InfoLineConfigWidget::new);
        Registry.CONFIG_WIDGET.registerConfigWidgetFactory(RendererToggle.class, RendererToggleConfigWidget::new);
        Registry.CONFIG_WIDGET.registerConfigWidgetFactory(StructureToggle.class, StructureToggleConfigWidget::new);

        Registry.CONFIG_WIDGET.registerConfigSearchInfo(InfoLineToggle.class, new ConfigSearchInfo<InfoLineToggle>(true, true).setBooleanStorageGetter(InfoLineToggle::getBooleanConfig).setKeyBindGetter(InfoLineToggle::getKeyBind));
        Registry.CONFIG_WIDGET.registerConfigSearchInfo(RendererToggle.class, new ConfigSearchInfo<RendererToggle>(true, true).setBooleanStorageGetter(RendererToggle::getBooleanConfig).setKeyBindGetter(RendererToggle::getKeyBind));
        Registry.CONFIG_WIDGET.registerConfigSearchInfo(StructureToggle.class, new ConfigSearchInfo<StructureToggle>(true, true).setBooleanStorageGetter(StructureToggle::getBooleanConfig).setKeyBindGetter(StructureToggle::getKeyBind));

        Registry.CONFIG_STATUS_WIDGET.registerConfigStatusWidgetFactory(RendererToggle.class, RendererToggleConfigStatusWidget::new, "minihud:csi_value_renderer_toggle");
        Registry.CONFIG_STATUS_WIDGET.registerConfigStatusWidgetFactory(StructureToggle.class, StructureRendererConfigStatusWidget::new, "minihud:csi_value_structure_toggle");

        Registry.HOTKEY_MANAGER.registerHotkeyProvider(MiniHudHotkeyProvider.INSTANCE);

        RenderHandler renderer = RenderHandler.INSTANCE;
        Registry.RENDER_EVENT_DISPATCHER.registerGameOverlayRenderer(renderer);
        Registry.RENDER_EVENT_DISPATCHER.registerTooltipPostRenderer(renderer);
        Registry.RENDER_EVENT_DISPATCHER.registerWorldPostRenderer(renderer);

        Registry.CLIENT_WORLD_CHANGE_EVENT_DISPATCHER.registerClientWorldChangeHandler(new ClientWorldChangeHandler());

        Registry.TICK_EVENT_DISPATCHER.registerClientTickHandler(new ClientTickHandler());

        Registry.CLIENT_PACKET_CHANNEL_HANDLER.registerClientChannelHandler(ServuxInfoSubRegistrationPacketHandler.INSTANCE);
        Registry.CLIENT_PACKET_CHANNEL_HANDLER.registerClientChannelHandler(ServuxInfoSubDataPacketHandler.INSTANCE);

        Actions.init();
        ConfigCallbacks.init();
    }
}
