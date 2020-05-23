import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent } from '../../../common';

const CreateProductPage = () => {

	const history = useHistory();

	const onSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);
		formData.price = parseFloat(formData.price).toFixed(2);
		formData.quantity = parseInt(formData.quantity)

		const result = await axios.post('/api/products', formData,
			{ validateStatus: false });
		if (result.status !== 200) {
			alert('Some error occured');
			console.log(result);
			return;
		}

		history.push('/manage/products');
	};

	const [state, setState] = useState({ loading: true, categories: null });
	useEffect(() => {
		const fetchCategories = async () => {
			const result = await axios.get('/api/categories', { validateStatus: false });
			if (result.status !== 200) {
				alert('Some error occured');
				setState({ loading: false, categories: null });
				return;
			}
			setState({ loading: false, categories: result.data });
		};
		fetchCategories();
	}, []);

	if (state.loading) return <></>
	else if (!state.categories) return <h3>Internal error</h3>
	else if (!state.categories.length) return <h3>Unable to create product - no categories exist</h3>
	return (
		<>
			<h3>Create Product</h3>

			<form onSubmit={onSubmit}>
				<div className="form-group">
					<label>Name</label>
					<input name="name" type="text" className="form-control" id="nameInput" placeholder="Name..." required />
				</div>

				<div className="form-group">
					<label>Description</label>
					<input name="description" type="text" className="form-control" id="descriptionInput" placeholder="Description..." required />
				</div>

				<div className="form-group">
					<label>Price</label>
					<input name="price" type="number" min={0} step="0.01" className="form-control" id="descriptionInput" placeholder="0.00" required />
				</div>

				<div className="form-group">
					<label>Quantity</label>
					<input name="quantity" type="number" step="1" min={1} className="form-control" id="descriptionInput" placeholder="0" required />
				</div>

				<div className="form-group">
					<label>Category</label>
					<select name="categoryId" className="custom-select" size="6" required>
						{
							state.categories.map((cat, i) =>
								<option key={`opt-${cat.id}`} value={cat.id}>{cat.name}</option>
							)
						}
					</select>
				</div>

				<button className="btn btn-success w-25" type="submit">
					Create
				</button>
			</form>
		</>
	);
};

export default CreateProductPage;