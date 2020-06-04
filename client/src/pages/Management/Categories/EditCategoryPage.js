import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, getFormDataJsonFromEvent } from '../../../common/Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import CategoryForm from './components/CategoryForm';

const EditCategoryPage = (props) => {
	const categoryId = props.match.params.id;
	const history = useHistory();

	const [validationErrors, setValidationErrors] = useState(null);

	const onSubmit = async event => {
		event.preventDefault();
		const formData = getFormDataJsonFromEvent(event);
		const result = await axios.put('/api/categories', formData, {
			headers: { 'X-Auth-Token': props.auth.token },
			validateStatus: false
		}
		);
		if (result.status === 400) {
			setValidationErrors(result.data.obj.map(r => r.msg));
			return;
		}
		else if (result.status !== 200) {
			alert('Some error occured');
			return;
		}
		history.push(`/manage/categories/${categoryId}`);
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
	else if (!state.category) return <h3>Category Id '{categoryId}' does not exist</h3>
	else return <>
		<h3>Edit category</h3>

		<CategoryForm initState={{ categoryId, categoryName: state.category.name }}
			onSubmitCb={onSubmit}
			validationErrors={validationErrors} />
	</>
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(EditCategoryPage);