import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { isValidUUID, doRequest } from '../../../Utils';
import Modal from '../../../components/Modal';
import { useHistory } from 'react-router-dom';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const CategoryPage = (props) => {
	const categoryId = props.match.params.id;
	const history = useHistory();

	const onEdit = async () => history.push(`/manage/categories/${categoryId}/edit`);

	const onDelete = async () => {
		try {
			const action = async () => axios.delete('/api/categories', {
				headers: { 'X-Auth-Token': props.auth.token },
				validateStatus: false,
				data: { id: categoryId }
			})
			await doRequest(action);
			history.push('/manage/categories');
		} catch (error) {
			alert(`${error} error occured`);
		}
	}

	const [validationErrors, setValidationErrors] = useState(null);
	const [state, setState] = useState({ loading: true, category: null });
	useEffect(() => {
		const fetchCategory = async () => {
			try {
				const action = async () => axios.get(`/api/categories/${categoryId}`);
				const result = await doRequest(action);
				setState({ loading: false, category: result });
			} catch (error) {
				alert(`${error} error occured`);
			}
		};

		if (isValidUUID(categoryId)) fetchCategory();
	}, []);


	if (!isValidUUID(categoryId)) return <h3>Category Id '{categoryId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else {
		if (!state.category) return <h3>Category Id '{categoryId}' does not exist</h3>
		else return <>
			<p>
				<b>Id:</b> {state.category.id}
			</p>

			<p>
				<b>Name:</b> {state.category.name}
			</p>

			<button type="button" className="btn btn-primary w-25 mr-2" onClick={onEdit}>
				Edit
			</button>

			<Modal title={"Are you sure"}
				btnText={"Delete"}
				btnClasses={"btn btn-danger w-25"}
				modalTitle={"Are you sure?"}
				modalPrimaryBtnText={"Delete"}
				modalPrimaryBtnClasses={"btn btn-danger"}
				onModalPrimaryBtnClick={onDelete}
				modalSecondaryBtnText={"Cancel"}
				modalSecondaryBtnClasses={"btn btn-secondary"}
			/>

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
		</>
	}
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(CategoryPage);