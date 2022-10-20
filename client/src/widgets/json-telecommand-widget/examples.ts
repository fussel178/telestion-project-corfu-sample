const examples = [
	{
		name: 'NOP Telecommand',
		content: {
			commandIndex: 0,
			sequenceCounter: 0,
			timeToExecute: 0,
			node: {
				type: 'fake-transceiver',
				hardware: 'I_D0'
			},
			app: {
				type: 'housekeeper',
				payload: {
					type: 'NOP'
				}
			}
		}
	},
	{
		name: 'Send Histogram',
		content: {
			commandIndex: 0,
			sequenceCounter: 0,
			timeToExecute: 0,
			node: {
				type: 'fake-transceiver',
				hardware: 'I_D0'
			},
			app: {
				type: 'housekeeper',
				payload: {
					type: 'SendHistogram'
				}
			}
		}
	},
	{
		name: 'Set Collect Interval',
		content: {
			commandIndex: 0,
			sequenceCounter: 0,
			timeToExecute: 0,
			node: {
				type: 'fake-transceiver',
				hardware: 'I_D0'
			},
			app: {
				type: 'housekeeper',
				payload: {
					type: 'SetCollectInterval',
					intervalInSeconds: 10
				}
			}
		}
	}
];

export function getTelecommand(key: string | number): string {
	return typeof key === 'number' && key < examples.length
		? JSON.stringify(examples[key].content, null, 2)
		: '';
}

export const pickerItems = examples.map((part, index) => ({
	id: index,
	name: part.name
}));
