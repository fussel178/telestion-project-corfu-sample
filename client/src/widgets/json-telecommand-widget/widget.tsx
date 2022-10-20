import { useCallback, useEffect, useMemo, useState } from 'react';
import {
	Divider,
	Flex,
	Heading,
	View,
	Picker,
	Item,
	TextArea,
	Button
} from '@adobe/react-spectrum';
import {
	BaseRendererProps,
	GenericProps
} from '@wuespace/telestion-client-types';

import { getTelecommand, pickerItems } from './examples';
import './style.css';
import { useBroadcast } from '@wuespace/telestion-client-core';

function isValidTelecommand(value: string): boolean {
	try {
		const obj = JSON.parse(value);

		// basic existence check
		let value1, value2;
		value1 = obj['commandIndex'];
		value1 = obj['sequenceCounter'];
		value1 = obj['timeToExecute'];
		value1 = obj['node'];
		value2 = value1['type'];
		value2 = value1['hardware'];
		value1 = obj['app'];
		value2 = value1['type'];
		value2 = value1['payload'];

		return true;
	} catch (err) {
		console.error(err);
		return false;
	}
}

export interface WidgetProps extends GenericProps {
	title: string;
}

export function Widget({ title }: BaseRendererProps<WidgetProps>) {
	const broadcast = useBroadcast('pretty-corfu-telecommand');
	const [pickerState, setPickerState] = useState<string | number>(0);
	const [value, setValue] = useState('');

	useEffect(() => setValue(getTelecommand(pickerState)), [pickerState]);
	const isValid = useMemo(() => isValidTelecommand(value), [value]);

	const sendTelecommand = useCallback(
		(value: string) => broadcast(JSON.parse(value)),
		[broadcast]
	);

	return (
		<View width="100%" height="100%">
			<Flex direction="column" width="100%" height="100%">
				<Heading level={3} margin="size-200" marginBottom="size-100">
					{title}
				</Heading>
				<Divider size="S" />
				<View padding="size-200" width="100%" height="100%">
					<Flex direction="column" width="100%" height="100%">
						<Picker
							width="100%"
							label="Pick predefined telecommand"
							items={pickerItems}
							selectedKey={pickerState}
							onSelectionChange={setPickerState}
						>
							{item => <Item>{item.name}</Item>}
						</Picker>
						<TextArea
							width="100%"
							flexGrow={1}
							label="JSON Telecommand"
							value={value}
							onChange={setValue}
							validationState={isValid ? 'valid' : 'invalid'}
							UNSAFE_className="json-tc-textarea"
						/>
						<Button
							variant="cta"
							isDisabled={!isValid}
							onPress={() => sendTelecommand(value)}
						>
							Send
						</Button>
					</Flex>
				</View>
			</Flex>
		</View>
	);
}
