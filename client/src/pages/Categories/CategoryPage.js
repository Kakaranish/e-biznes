import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { isValidUUID } from '../../common';
import Modal from '../../Modal';
import { useHistory } from 'react-router-dom';

const CategoryPage = (props) => {
	const categoryId = props.match.params.id;
	const history = useHistory();

	const onEdit = async () => history.push(`/categories/${categoryId}/edit`);

	const onDelete = async () => {
		const result = await axios.delete('/api/categories',
			{ validateStatus: false, data: { id: categoryId } })
		if (result.status !== 200) {
			alert('Some error occured');
			return;
		}
		alert('Removed');
		history.push('/categories');
	}

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
		</>
	}
};

export default CategoryPage;