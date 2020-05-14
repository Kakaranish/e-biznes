import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { getFormDataJsonFromEvent } from '../../common';
import axios from 'axios';

const CreateCategoryPage = () => {
	const history = useHistory();

	const [validationErrors, setValidationErrors] = useState(null);

	const onSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);
		const result = await axios.post('/api/categories', formData,
			{ validateStatus: false });
		if (result.status !== 200) {
			setValidationErrors(result.data.obj.map(r => r.msg));
			return;
		}
		history.push('/categories');
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

export default CreateCategoryPage;