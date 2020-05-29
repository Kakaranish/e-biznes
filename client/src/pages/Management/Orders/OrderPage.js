import React, { useState, useEffect } from 'react';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import ShippingInfo from './components/ShippingInfo';
import OrderedProducts from '../../../components/Orders/OrderedProducts';
import Payments from './components/Payments';
import BasicOrderInfo from '../../../components/Orders/BasicOrderInfo';
import { fetchOrderInfoAsAdmin } from '../../../common/orders-common';

const OrderPage = (props) => {

    const orderId = props.match.params.id;

    const [state, setState] = useState({ loading: true, orderInfo: null });
    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const result = await fetchOrderInfoAsAdmin(props.auth, orderId);
                if(result) setState(Object.assign({loading: false}, result));
            } catch (error) { alert(`${error} error occured`); }
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