import React, { useState, useEffect } from 'react';
import { useHistory, Link } from 'react-router-dom';
import axios from 'axios';
import { isValidUUID, doRequest } from '../../Utils';
import ProductWishlistStatus from './ProductWishlistStatus';
import * as Utils from '../../Utils';
import Modal from '../../components/Modal';
import AddOpinion from './Components/AddOpinion';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

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

	const deleteOpinionOnClick = async opinionId => {
		try {
			const action = async () => axios.delete('/api/opinions', {
				data: { opinionId: opinionId },
				validateStatus: false,
				headers: { 'X-Auth-Token': props.auth.token }
			});
			await doRequest(action);

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

			<h3 className="mb-4">Opinions</h3>
			{
				!state.result.opinions || state.result.opinions.length === 0
					? <p>No opinions yet</p>
					: <>
						{
							state.result.opinions.map(o =>
								<div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`o-${o.opinion.id}`}>
									<p className="mb-1">
										<b>{o.user.firstName} {o.user.lastName}</b> ({o.user.email}) wrote:
									</p>

									<p>{o.opinion.content}</p>

									{
										state.result.userId === o.user.id && <>
											<Link to={'/'} className="btn btn-primary w-25">
												Edit
												</Link>

											<Modal title={"Are you sure"}
												btnText={"Delete"}
												btnClasses={"btn btn-danger w-25"}
												modalTitle={"Are you sure?"}
												modalPrimaryBtnText={"Delete"}
												modalPrimaryBtnClasses={"btn btn-danger"}
												onModalPrimaryBtnClick={async () => deleteOpinionOnClick(o.opinion.id)}
												modalSecondaryBtnText={"Cancel"}
												modalSecondaryBtnClasses={"btn btn-secondary"}
											/>
										</>
									}
								</div>
							)
						}
					</>

			}

			{
				state.result.allowAddOpinion &&
				<AddOpinion productId={productId} callback={() => history.go()} />
			}

		</>
	}
};


export default new AwareComponentBuilder()
	.withAuthAwareness()
	.withCartAwareness()
	.build(ProductPage);