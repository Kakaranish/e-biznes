import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import { doRequest } from '../../../common/Utils';
import ShippingInfo from './components/ShippingInfo';
import OrderedProducts from '../../../components/Orders/OrderedProducts';
import Payments from './components/Payments';
import BasicOrderInfo from '../../../components/Orders/BasicOrderInfo';

const OrderPage = (props) => {

    const orderId = props.match.params.id;

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            let result;
            try {
                const action = async () => axios.get(`/api/admin/orders/${orderId}`, {
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
                if (payment.status === "ACCEPTED") paymentsValue += payment.amountOfMoney
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

        <BasicOrderInfo orderState={state} showUser={true} />

        <ShippingInfo shippingInfo={state.orderInfo.shippingInfo} />

        <Payments payments={state.orderInfo.payments} />

        <OrderedProducts cartItems={state.orderInfo.cartItems} />
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(OrderPage);