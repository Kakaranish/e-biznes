import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { isValidUUID } from '../../common';

const CategoryPage = (props) => {
	const categoryId = props.match.params.id;

	const [state, setState] = useState({ loading: true, category: null });
	useEffect(() => {
		const fetchCategory = async () => {
			const result = await axios.get(`/api/categories/${categoryId}`);
			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}
			setState({loading: false, category: result.data});
		};

		if (isValidUUID(categoryId)) fetchCategory();
	}, []);


	if (!isValidUUID(categoryId)) return <h3>Category Id '{categoryId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else return <>
		<p>
			<b>Id:</b> {state.category.id}
		</p>
		
		<p>
			<b>Name:</b> {state.category.name}
		</p>
	</>
};

export default CategoryPage;