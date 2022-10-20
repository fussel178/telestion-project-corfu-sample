import { Widget } from '@wuespace/telestion-client-types';
import { Widget as WidgetRenderer, WidgetProps } from './widget';

export const widget: Widget<WidgetProps> = {
	name: 'jsonTelecommandWidget',
	title: 'json-telecommand-widget',
	version: '0.0.0',
	Widget: WidgetRenderer
};
