import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { createAuthAwareComponent } from '../Auth/Utils';
import { getFormDataJsonFromEvent } from '../../common';

const PaymentPage = (props) => {

	const orderId = props.match.params.id;
	const history = useHistory();

	const paymentMethods = [
		{ code: "BLIK", name: "Blik" },
		{ code: "TRANSFER", name: "Transfer" },
		{ code: "CARD", name: "Credit Card" }
	];
	const payOnClick = async event => {
		event.preventDefault();
		let formData = getFormDataJsonFromEvent(event);
		formData.amountOfMoney = parseFloat(formData.amountOfMoney);
		formData.orderId = orderId;

		const result = await axios.post('/api/payments', formData, {
			headers: { 'X-Auth-Token': props.auth.token },
			validateStatus: false
		});
		if (result.status !== 200) {
			alert('Some error occured');
			console.log(result);
			return;
		}
		
		history.push(`/orders/${orderId}`);
	}

	const [state, setState] = useState({ loading: true, orderInfo: null });
	useEffect(() => {
		const fetchOrder = async () => {
			const result = await axios.get(`/api/orders/${orderId}`, {
				headers: { 'X-Auth-Token': props.auth.token },
				validateStatus: false
			});
			if (result.status !== 200) {
				alert('Some error occured');
				return;
			}

			let totalPrice = 0;
			result.data.cartItems.forEach(ci => totalPrice += ci.cartItem.quantity * ci.product.price);

			let paymentsValue = 0;
			result.data.payments.forEach(payment => paymentsValue += payment.amountOfMoney);

			const toPay = parseFloat((totalPrice - paymentsValue).toFixed(2));

			console.log(result.data);
			setState({
				loading: false,
				orderInfo: result.data,
				totalPrice: totalPrice,
				paymentsValue: paymentsValue,
				toPay: toPay
			});
		};

		fetchOrder();
	}, []);

	if (state.loading) return <></>
	else if (!state.orderInfo) return <h3>No such order...</h3>
	return <>
		<h3>Pay for order '{orderId}'</h3>

		<p>
			<b>Total Price:</b> {state.totalPrice.toFixed(2)} PLN
        </p>

		<p>
			<b>Money paid in: </b> {state.paymentsValue.toFixed(2)} PLN
        </p>

		<p>
			<b>Remaining money to paid:</b> {state.toPay.toFixed(2)} PLN
        </p>

		<form onSubmit={payOnClick} className="form-inline">

			<div className="form-group mb-3">
				<label className="mr-3">Payment method</label>
				<select name="methodCode" className="form-control" style={{ width: "auto" }}>
					{
						paymentMethods.map(m =>
							<option value={m.code} key={`opt-${m.code}`}>
								{m.name}
							</option>
						)
					}
				</select>
			</div>

			<div className="form-group w-100">
				<input name="amountOfMoney" type="number" step="0.01" min="0" max={state.toPay}
					className="form-control form-control mr-1" style={{ width: "10%" }}
					defaultValue={(state.totalPrice - state.paymentsValue).toFixed(2)}
					placeholder="Amount of money" required
				/>

				<button type="submit" className="btn btn-primary w-25 mr-2">
					Pay
        		</button>
			</div>
		</form>
	</>
};

export default createAuthAwareComponent(PaymentPage);