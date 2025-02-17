/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {normalizeFieldName} from 'dynamic-data-mapping-form-renderer';
import React, {useRef} from 'react';

import {FieldBase} from '../FieldBase/ReactFieldBase.es';
import Text from '../Text/Text.es';
import {useSyncValue} from '../hooks/useSyncValue.es';

const KeyValue = ({disabled, onChange, value, ...otherProps}) => (
	<div className="active form-text key-value-editor">
		<label className="control-label key-value-label">
			{Liferay.Language.get('field-id')}:
		</label>

		<input
			{...otherProps}
			className={`${disabled ? 'disabled ' : ''}key-value-input`}
			onChange={(event) => {
				const value = normalizeFieldName(event.target.value);
				onChange({target: {value}});
			}}
			readOnly={disabled}
			type="text"
			value={value}
		/>
	</div>
);

const Main = ({
	generateKeyword: initialGenerateKeyword = true,
	keyword: initialKeyword,
	keywordReadOnly,
	name,
	onBlur,
	onChange,
	onFocus,
	onKeywordBlur,
	onKeywordChange,
	placeholder,
	readOnly,
	required,
	showLabel,
	spritemap,
	value,
	visible,
	...otherProps
}) => {
	const [keyword, setKeyword] = useSyncValue(initialKeyword);

	const generateKeywordRef = useRef(initialGenerateKeyword);

	return (
		<FieldBase
			{...otherProps}
			name={name}
			readOnly={readOnly}
			required={required}
			showLabel={showLabel}
			spritemap={spritemap}
			visible={visible}
		>
			<Text
				name={`keyValueLabel${name}`}
				onBlur={onBlur}
				onChange={(event) => {
					const {value} = event.target;

					onChange(event);

					if (generateKeywordRef.current) {
						const newKeyword = normalizeFieldName(value);
						onKeywordChange(event, newKeyword, true);
					}
				}}
				onFocus={onFocus}
				placeholder={placeholder}
				readOnly={readOnly}
				required={required}
				showLabel={showLabel}
				spritemap={spritemap}
				syncDelay={false}
				value={value}
				visible={visible}
			/>
			<KeyValue
				disabled={keywordReadOnly}
				onBlur={onKeywordBlur}
				onChange={(event) => {
					const {value} = event.target;

					generateKeywordRef.current = false;
					onKeywordChange(event, value, false);
					setKeyword(value);
				}}
				value={keyword}
			/>
		</FieldBase>
	);
};

Main.displayName = 'KeyValue';

export default Main;
