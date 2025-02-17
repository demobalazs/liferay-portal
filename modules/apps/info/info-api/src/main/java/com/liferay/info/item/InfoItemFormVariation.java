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

package com.liferay.info.item;

import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.petra.lang.HashUtil;

import java.util.Objects;

/**
 * @author Jorge Ferrer
 */
public class InfoItemFormVariation {

	public InfoItemFormVariation(
		String key, InfoLocalizedValue<String> labelInfoLocalizedValue) {

		_key = key;
		_labelInfoLocalizedValue = labelInfoLocalizedValue;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof InfoItemFormVariation)) {
			return false;
		}

		InfoItemFormVariation infoItemFormVariation =
			(InfoItemFormVariation)object;

		if (Objects.equals(_key, infoItemFormVariation._key)) {
			return true;
		}

		return false;
	}

	public String getKey() {
		return _key;
	}

	public InfoLocalizedValue<String> getLabelInfoLocalizedValue() {
		return _labelInfoLocalizedValue;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, _key);
	}

	private final String _key;
	private final InfoLocalizedValue<String> _labelInfoLocalizedValue;

}