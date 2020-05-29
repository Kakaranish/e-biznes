import axios from 'axios';
import { doRequest } from './Utils';

export const fetchOrder = async (auth, orderId) => {
    let result;
    try {
        const uri = auth.token === "ADMIN"
            ? `/api/admin/orders/${orderId}`
            : `/api/orders/${orderId}`;

        const action = async () => axios.get(uri, {
            headers: { 'X-Auth-Token': auth.token },
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

    return {
        orderInfo: result,
        totalPrice: totalPrice,
        paymentsValue: paymentsValue,
        toPay: toPay
    };
};