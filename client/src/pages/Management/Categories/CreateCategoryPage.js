import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { getFormDataJsonFromEvent, doRequest } from '../../../Utils';
import axios from 'axios';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const CreateCategoryPage = (props) => {
	const history = useHistory();

	const [validationErrors, setValidationErrors] = useState(null);

	const onSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);

		let result;
		try {
			const action = async () => axios.post('/api/categories', formData,{
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
			result = await doRequest(action);
			history.push('/manage/categories');
		} catch (error) {
			if (error === 400) {
				setValidationErrors(result.obj.map(r => r.msg));
				return;
			}
			alert(`${error} error occured`);
		}
	};

	return (
		<>
			<h3>Create category</h3>
			<form onSubmit={onSubmit}>
				<div className="form-group">
					<input name="name" type="text" className="form-control" id="nameInput" placeholder="Name..." required />
				</div>

				<button type="submit" className="btn btn-success w-25">
					Submit
				</button>

				{
					validationErrors &&
					<div className="col-12 mt-2">
						<p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
							Validation errors
                        </p>
						<ul style={{ paddingTop: "0" }, { marginTop: "0px" }}>
							{
								validationErrors.map((error, i) => {
									return <li key={`val-err-${i}`} className="text-danger">{error}</li>
								})
							}
						</ul>
					</div>
				}
			</form>
		</>
	);
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(CreateCategoryPage);