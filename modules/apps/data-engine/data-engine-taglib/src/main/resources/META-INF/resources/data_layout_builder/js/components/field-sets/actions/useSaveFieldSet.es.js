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

import {useContext} from 'react';

import AppContext from '../../../AppContext.es';
import {UPDATE_DATA_DEFINITION, UPDATE_FIELDSETS} from '../../../actions.es';
import DataLayoutBuilderContext from '../../../data-layout-builder/DataLayoutBuilderContext.es';
import {updateItem} from '../../../utils/client.es';
import {getDataDefinitionFieldSet} from '../../../utils/dataDefinition.es';
import {
	containsField,
	normalizeDataLayoutRows,
} from '../../../utils/dataLayoutVisitor.es';
import saveDataDefinition from '../../../utils/saveDataDefinition.es';
import {errorToast, successToast} from '../../../utils/toast.es';

export default ({
	DataLayout,
	availableLanguageIds,
	childrenContext,
	fieldSet,
}) => {
	const [context, dispatch] = useContext(AppContext);
	const [dataLayoutBuilder] = useContext(DataLayoutBuilderContext);
	const {dataDefinition, dataLayout, fieldSets} = context;
	const {state: childrenState} = childrenContext;

	return (name) => {
		const {
			dataDefinition: {dataDefinitionFields},
			dataLayout: {dataLayoutPages},
		} = childrenState;

		const normalizedFieldSet = {
			...fieldSet,
			availableLanguageIds,
			dataDefinitionFields,
			defaultDataLayout: {
				...fieldSet.defaultDataLayout,
				dataLayoutPages,
			},
			name,
		};

		return updateItem(
			`/o/data-engine/v2.0/data-definitions/${fieldSet.id}`,
			normalizedFieldSet
		)
			.then(() => {
				const dataDefinitionFieldSet = getDataDefinitionFieldSet(
					dataDefinition.dataDefinitionFields,
					fieldSet.id
				);

				const normalizedDataDefinitionFields = () =>
					dataDefinition.dataDefinitionFields.map((field) => {
						const {
							customProperties: {ddmStructureId},
						} = field;

						if (ddmStructureId == fieldSet.id) {
							return {
								...field,
								label: name,
								nestedDataDefinitionFields: dataDefinitionFields,
							};
						}

						return field;
					});

				if (dataDefinitionFieldSet) {
					const fieldName = dataDefinitionFieldSet.name;

					if (containsField(dataLayout.dataLayoutPages, fieldName)) {
						dataLayoutBuilder.dispatch('fieldEditedProperties', {
							fieldName,
							properties: [
								{
									name: 'nestedFields',
									value: dataDefinitionFields.map(({name}) =>
										DataLayout.getDDMFormField(
											childrenState.dataDefinition,
											name
										)
									),
								},
								{
									name: 'rows',
									value: normalizeDataLayoutRows(
										dataLayoutPages
									),
								},
								{
									name: 'label',
									value: name.en_US,
								},
							],
						});
					}
					else {
						dispatch({
							payload: {
								dataDefinition: {
									...dataDefinition,
									dataDefinitionFields: normalizedDataDefinitionFields(),
								},
							},
							type: UPDATE_DATA_DEFINITION,
						});
					}

					return saveDataDefinition({
						...context,
						dataDefinition: {
							...dataDefinition,
							dataDefinitionFields: normalizedDataDefinitionFields(),
						},
					});
				}

				return Promise.resolve();
			})
			.then(() => {
				dispatch({
					payload: {
						fieldSets: fieldSets.map((field) => {
							if (fieldSet.id === field.id) {
								return normalizedFieldSet;
							}

							return field;
						}),
					},
					type: UPDATE_FIELDSETS,
				});

				successToast(Liferay.Language.get('fieldset-saved'));

				return Promise.resolve();
			})
			.catch(({message}) => errorToast(message));
	};
};
