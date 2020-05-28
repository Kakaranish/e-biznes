import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { isValidUUID, getFormDataJsonFromEvent, doRequest } from '../../../Utils';
import axios from 'axios';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const EditProductPage = (props) => {

	const history = useHistory();
	const productId = props.match.params.id;

	const onSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);
		formData.price = parseFloat(formData.price);
		formData.quantity = parseInt(formData.quantity);

		try {
			const action = async () => axios.put('/api/products', formData,{
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
			});
			await doRequest(action);
			history.push(`/manage/products/${productId}`);
		} catch (error) {
			alert(`${error} error occured`);
		}
	};

	const [state, setState] = useState({
		loading: true, categories: null,
		product: null, error: null
	});
	useEffect(() => {
		const fetchData = async () => {

			let productResult;
			try {
				const action = async () => axios.get(`/api/products/${productId}`,
					{ validateStatus: false });
				productResult = await doRequest(action);
			} catch (error) {
				if (error === 404) setState({ loading: false });
				alert(`${error} error occured while loading product`);
				return;
			}

			let categoriesResult;
			try {
				const action = async () => axios.get('/api/categories',
					{ validateStatus: false });
				categoriesResult = await doRequest(action);
			} catch (error) {
				alert(`${error} error occured`);
				setState({ loading: false })
				return;
			}

			setState({
				loading: false,
				product: productResult.product,
				categories: categoriesResult
			});
		};

		if (isValidUUID(productId)) fetchData();
	}, []);

	if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else {
		if (state.error) return <h3>Error: {state.error}</h3>
		else if (!state.product) return <h3>No product with id '{productId}'</h3>
		else if (!state.categories) return <h3>No categories found</h3>
		else return (
			<>
				<h3>Edit product</h3>
				<form onSubmit={onSubmit}>
					<div className="form-group">
						<label>Id</label>
						<input name="id" type="text" className="form-control" id="idInput" value={state.product.id} readOnly />
					</div>

					<div className="form-group">
						<label>Name</label>
						<input name="name" type="text" className="form-control" id="nameInput" placeholder="Name..." defaultValue={state.product.name} required />
					</div>

					<div className="form-group">
						<label>Description</label>
						<input name="description" type="text" className="form-control" id="descriptionInput" placeholder="Description..."
							defaultValue={state.product.description} required />
					</div>

					<div className="form-group">
						<label>Price</label>
						<input name="price" type="number" min={0} step="0.01" className="form-control" id="descriptionInput"
							placeholder="0.00" defaultValue={state.product.price} required />
					</div>

					<div className="form-group">
						<label>Quantity</label>
						<input name="quantity" type="number" step="1" min={1} className="form-control"
							defaultValue={state.product.quantity} id="descriptionInput" placeholder="0" required />
					</div>

					<div className="form-group">
						<label>Category</label>
						<select name="categoryId" className="custom-select" size="6" required>
							{
								state.categories.map((prod, i) =>
									<option selected={prod.id == state.product.categoryId} key={`opt-${prod.id}`} value={prod.id}>
										{prod.name}
									</option>
								)
							}
						</select>
					</div>
					<button type="submit" className="btn btn-success w-25">
						Update Product
					</button>
				</form>
			</>
		);
	}
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.build(EditProductPage);