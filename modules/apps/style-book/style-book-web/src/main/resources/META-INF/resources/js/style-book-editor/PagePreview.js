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

import React, {useCallback, useContext, useEffect, useRef} from 'react';

import {StyleBookContext} from './StyleBookContext';
import {config} from './config';

export default function PagePreview() {
	const iframeRef = useRef();

	const {tokensValues = {}} = useContext(StyleBookContext);

	const loadTokenValues = useCallback(() => {
		if (iframeRef.current) {
			Object.values(tokensValues).forEach(
				({cssVariableMapping, value}) => {
					iframeRef.current.contentDocument.body.style.setProperty(
						`--${cssVariableMapping}`,
						value
					);
				}
			);
		}
	}, [tokensValues]);

	useEffect(() => {
		loadTokenValues(iframeRef.current, tokensValues);

		const iframeLiferay = iframeRef.current?.contentWindow?.Liferay;

		if (iframeLiferay) {
			iframeRef.current.contentWindow.Liferay.on(
				'endNavigate',
				loadTokenValues
			);
		}

		return () => {
			if (iframeLiferay) {
				iframeLiferay.detach('endNavigate', loadTokenValues);
			}
		};
	}, [loadTokenValues, tokensValues]);

	return (
		<div className="style-book-editor__page-preview">
			<iframe
				className="style-book-editor__page-preview-frame"
				onLoad={() => {
					if (iframeRef.current?.contentWindow?.Liferay) {
						iframeRef.current.contentWindow.Liferay.on(
							'endNavigate',
							loadTokenValues
						);
					}
					loadTokenValues(iframeRef.current, tokensValues);
				}}
				ref={iframeRef}
				src={config.previewURL}
			/>
		</div>
	);
}
