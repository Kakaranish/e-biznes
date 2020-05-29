import axios from 'axios';
import { doRequest } from './Utils';

export const fetchOrderInfoAsUser = async (auth, orderId) => {
    const action = async () => axios.get(`/api/orders/${orderId}`, {
        headers: { 'X-Auth-Token': auth.token },
        validateStatus: false
    });
    const result = await doRequest(action);
    return prepareOrderInfo(result);
}

export const fetchOrderInfoAsAdmin = async (auth, orderId) => {
    const action = async () => axios.get(`/api/admin/orders/${orderId}`, {
        headers: { 'X-Auth-Token': auth.token },
        validateStatus: false
    });
    const result = await doRequest(action);
    return prepareOrderInfo(result);
}

const prepareOrderInfo = fetchedOrderResult => {
    if(!fetchedOrderResult) return;

    let totalPrice = 0;
    fetchedOrderResult.cartItems.forEach(ci => totalPrice += ci.cartItem.quantity * ci.cartItem.pricePerProduct);

    let paymentsValue = 0;
    fetchedOrderResult.payments.forEach(payment => {
        if (payment.status === "ACCEPTED") paymentsValue += payment.amountOfMoney
    });

    let toPay = parseFloat((totalPrice - paymentsValue).toFixed(2));
    
    return {
        orderInfo: fetchedOrderResult,
        totalPrice: totalPrice,
        paymentsValue: paymentsValue,
        toPay: toPay
    }
}