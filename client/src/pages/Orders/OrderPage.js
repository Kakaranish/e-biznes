import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';
import moment from 'moment';
import { doRequest } from '../../common/Utils';
import ShippingInfo from './components/ShippingInfo';
import Payments from './components/Payments';
import OrderedProducts from '../../components/Orders/OrderedProducts';

const OrderPage = (props) => {

    const orderId = props.match.params.id;

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            let result;
            try {
                const action = async () => axios.get(`/api/orders/${orderId}`, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                result = await doRequest(action);
            } catch (error) {
                alert(`${error} error occured`);
                return;
            }

            let totalPrice = 0;
            result.cartItems.forEach(ci => totalPrice += ci.cartItem.quantity * ci.cartItem.pricePerProduct);

            let paymentsValue = 0;
            result.payments.forEach(payment => {
                if(payment.status === "ACCEPTED") paymentsValue += payment.amountOfMoney
            });

            let toPay = parseFloat((totalPrice - paymentsValue).toFixed(2));

            setState({
                loading: false,
                orderInfo: result,
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

        <h3>Order {orderId}</h3>
        <p>Created: {moment(state.orderInfo.order.dateCreated).format('YYYY-MM-DD hh:mm:ss')}</p>

        <p>
            <b>Paid? </b>
            {
                state.toPay <= 0
                    ? <span className="text-success">Yes</span>
                    : <span className="text-danger">No</span>
            }
        </p>

        <p>
            <b>Total Price:</b> {state.totalPrice.toFixed(2)} PLN
        </p>

        <p>
            <b>Money paid in: </b> {state.paymentsValue.toFixed(2)} PLN
        </p>

        <p>
            <b>Remaining money to paid:</b> {state.toPay} PLN
        </p>

        {
            state.toPay > 0 &&
            <Link to={`/orders/${orderId}/payment`} className="btn btn-primary w-25 mb-5">
                Pay
            </Link>
        }

        <ShippingInfo orderId={orderId} shippingInfo={state.orderInfo.shippingInfo} />

        <Payments payments={state.orderInfo.payments} />

        <OrderedProducts cartItems={state.orderInfo.cartItems} />
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(OrderPage);