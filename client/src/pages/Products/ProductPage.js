import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID } from '../../common';
import ProductWishlistStatus from './ProductWishlistStatus';
import * as AuthUtils from '../Auth/Utils';
import { getFormDataJsonFromEvent } from '../../common';

const ProductPage = (props) => {

	const productId = props.match.params.id;
	const history = useHistory();

	const addToCartOnSubmit = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);
		formData.quantity = parseInt(formData.quantity);
		formData.productId = productId;

		const result = await axios.post('/api/cart/add', formData, {
			validateStatus: false,
			headers: { 'X-Auth-Token': props.auth.token }
		});

		if (result.status === 401) {
			alert('You are no longer authorized to do this. Please log in.');
			history.push('/auth/login');
			return;
		}
		if (result.status !== 200) {
			alert('Internal error. Try to refresh page.');
			console.log(result);
			return;
		}

		history.go();
	}

	const [state, setState] = useState({ loading: true, result: null });
	useEffect(() => {
		const fetchProduct = async () => {
			const headers = props.auth?.token ? { 'X-Auth-Token': props.auth.token } : {};
			const result = await axios.get(`/api/products/${productId}`, {
				headers: headers, validateStatus: false
			});

			if (result.status === 404) {
				setState({ loading: false, result: null });
				return;
			}
			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}
			setState({ loading: false, result: result.data });
		};

		if (isValidUUID(productId)) fetchProduct();
	}, []);

	if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else {
		if (!state.result) return <h3>Product Id '{productId}' does not exist</h3>
		else return <>
			<h3>
				{state.result.product.name}
				{props.auth && <ProductWishlistStatus productId={productId}
					initState={state.result.wishlistItem ? true : false} />}
			</h3>

			<p>
				<b>Is in cart? </b> {state.result.cartItem ? "Yes" : "No"}
			</p>

			<p>
				<b>Description:</b> {state.result.product.description}
			</p>

			<p>
				<b>Price:</b> {state.result.product.price.toFixed(2)}PLN
            </p>

			<p>
				<b>Available quantity:</b> {state.result.product.quantity}
			</p>

			<p>
				<b>Category:</b> {state.result.category?.name ?? 'None'}
			</p>

			{
				state.result.product.quantity === 0
					? <h3 className="text-danger">Product is not available</h3>
					: <form onSubmit={addToCartOnSubmit} className="form-inline">
						<div className="form-group w-100">
							<input name="quantity" type="number" step="1" min="0" max={state.result.product.quantity} className="form-control form-control"
								style={{ width: "10%" }} placeholder="Quantity to add" disabled={state.result.cartItem ? true : false} required />
							<button type="submit" className="btn btn-primary w-25 mr-2" disabled={state.result.cartItem ? true : false}>
								{
									state.result.cartItem
										? <>Product already in cart</>
										: <>Add to cart</>
								}
							</button>
						</div>
					</form>
			}
		</>
	}
};


export default AuthUtils.createAuthAwareComponent(ProductPage);