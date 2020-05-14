import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, getFormDataJsonFromEvent } from '../../common';

const EditCategoryPage = (props) => {
	const categoryId = props.match.params.id;
	const history = useHistory();

	const onSubmit = async event => {
		event.preventDefault();
		const formData = getFormDataJsonFromEvent(event);
		const result = await axios.put('/api/categories', formData, { validateStatus: false });
		if (result.status !== 200) {
			alert('Some error occured');
			return;
		}
		history.push(`/categories/${categoryId}`);
	};

	const [state, setState] = useState({ loading: true, category: null });
	useEffect(() => {
		const fetchCategory = async () => {
			const result = await axios.get(`/api/categories/${categoryId}`);
			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}
			setState({ loading: false, category: result.data });
		};

		if (isValidUUID(categoryId)) fetchCategory();
	}, []);

	if (!isValidUUID(categoryId)) return <h3>Category Id '{categoryId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else {
		if (!state.category) return <h3>Category Id '{categoryId}' does not exist</h3>
		else return <>
			<h3>Edit category</h3>

			<form onSubmit={onSubmit}>
				<div className="form-group">
					<input name="id" type="text" className="form-control" id="idInput" value={categoryId} readOnly />
				</div>
				<div className="form-group">
					<input name="name" type="text" className="form-control" id="nameInput" defaultValue={state.category.name} />
				</div>

				<button type="submit" className="btn btn-success w-25">
					Submit
				</button>
			</form>
		</>
	}
};

export default EditCategoryPage;