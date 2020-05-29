import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, doRequest } from '../../Utils';
import ProductWishlistStatus from './ProductWishlistStatus';
import * as Utils from '../../Utils';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import Opinions from './Components/Opinions';

const ProductPage = (props) => {

	const productId = props.match.params.id;
	const history = useHistory();

	const addToCartOnSubmit = async event => {
		event.preventDefault();
		let formData = Utils.getFormDataJsonFromEvent(event);
		formData.quantity = parseInt(formData.quantity);
		formData.productId = productId;

		try {
			const action = async () => axios.post('/api/cart/add', formData, {
				validateStatus: false,
				headers: { 'X-Auth-Token': props.auth.token }
			});
			await doRequest(action);
			props.addToCart(productId);

			history.go();
		} catch (error) {
			alert(`${error} error occured`);
		}
	}

	const [state, setState] = useState({ loading: true, result: null });
	useEffect(() => {
		const fetchProduct = async () => {

			try {
				const headers = props.auth?.token ? { 'X-Auth-Token': props.auth.token } : {};
				const action = async () => axios.get(`/api/products/${productId}`,
					{ headers: headers, validateStatus: false });
				const result = await doRequest(action);
				const allowAddOpinion = !result.opinions.some(o => o.user.id === result.userId)
					&& result.boughtByUser;
				setState({
					loading: false,
					result: Object.assign({ allowAddOpinion: allowAddOpinion }, result)
				});
			} catch (error) {
				if (error === 404) {
					setState({ loading: false, result: null });
					return;
				}
				alert(`${error} error occured`);
			}
		};

		if (isValidUUID(productId)) fetchProduct();
	}, []);

	// ---  RENDER  ------------------------------------------------------------------------------------------------------

	if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
	else if (state.loading) return <></>
	else if (!state.result) return <h3>Product Id '{productId}' does not exist</h3>
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
			<b>Category:</b>&nbsp;
			{
				state.result.category == null
					? 'None'
					: <>
						{state.result.category.name}
						{state.result.category?.isDeleted && <i>(DELETED)</i>}
					</>
			}
		</p>

		{
			state.result.product.quantity === 0
				? <h3 className="text-danger">Product is not available</h3>
				: <form onSubmit={addToCartOnSubmit} className="form-inline mb-4">
					<div className="form-group w-100">
						<input name="quantity" type="number" step="1" min="0" max={state.result.product.quantity} className="form-control form-control"
							style={{ width: "10%" }} placeholder="Quantity to add" disabled={state.result.cartItem ? true : false} required />
						<button type="submit" className="btn btn-primary w-25 mr-2" disabled={state.result.cartItem ? true : false}>
							{
								state.result.cartItem
									? "Product already in cart"
									: "Add to cart"
							}
						</button>
					</div>
				</form>
		}

		<Opinions state={state} />

	</>
};

export default new AwareComponentBuilder()
	.withAuthAwareness()
	.withCartAwareness()
	.build(ProductPage);