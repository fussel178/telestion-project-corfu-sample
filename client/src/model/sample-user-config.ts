import { UserConfig } from '@wuespace/telestion-client-types';

export const userConfig: UserConfig = {
	admin: {
		dashboards: [
			{
				title: 'Overview',
				columns: 2,
				rows: 2,
				widgets: [
					{
						id: '0',
						widgetName: 'currentValuesWidget',
						width: 1,
						height: 2,
						initialProps: {
							title: 'Current values',
							connections: []
						}
					},
					{
						id: '1',
						widgetName: 'jsonTelecommandWidget',
						width: 1,
						height: 2,
						initialProps: {
							title: 'JSON Telecommand'
						}
					}
				]
			}
		]
	}
};
