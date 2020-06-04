import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { getFormDataJsonFromEvent } from '../../../common/Utils';
import axios from 'axios';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import CategoryForm from './components/CategoryForm';

const CreateCategoryPage = (props) => {
	const history = useHistory();

	const [validationErrors, setValidationErrors] = useState(null);

	const onSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);

		const result = await axios.post('/api/categories', formData, {
			headers: { 'X-Auth-Token': props.auth.token },
			validateStatus: false
		});
		if (result.status === 400) {
			setValidationErrors(result.data.obj.map(r => r.msg));
			return;
		}
		else if (result.status !== 200) {
			alert("Some error occured");
			console.log(result);
			return;
		}
		history.push('/manage/categories');
	};

	return <>
		<h3>Create category</h3>

		<CategoryForm initState={null}
			onSubmitCb={onSubmit}
			validationErrors={validationErrors} />
	</>
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(CreateCategoryPage);