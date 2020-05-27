import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import axios from 'axios';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

const CartPage = (props) => {

    const history = useHistory();

    const finalizeOnClick = async () => {
        const result = await axios.post('/api/orders', { cartId: state.cartItems[0].cartItem.cartId }, {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });
        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result.data);
            return;
        }

        history.push(`/orders/${result.data}`);
    }

    const deleteFromCart = async (cartItemId, productId) => {
        const result = await axios.post('/api/cart/delete', { cartItemId: cartItemId }, {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false
        });
        if (result.status !== 200) {
            alert('Some error occured');
            console.log(result.data);
            return;
        }

        props.removeFromCart(productId);
        history.go();
    }

    const validateCart = async (cartItems) => {
        cartItems.forEach(async ci => {
            if(ci.cartItem.quantity > ci.product.quantity) {
                const result = await axios.post('/api/cart/delete', { cartItemId: ci.cartItem.id }, {
                    headers: { 'X-Auth-Token': props.auth.token },
                    validateStatus: false
                });
                if (result.status !== 200) {
                    alert('Some error occured');
                    console.log(result.data);
                    return;
                }

                alert(`Product ${ci.product.name} is no longer available in quantity you have chosen. It will be removed from your cart.`);
                history.go();
            }
        })
    }

    const [state, setState] = useState({ loading: true, cartItems: null });
    useEffect(() => {
        const fetchCartItems = async () => {
            const result = await axios.get('/api/cart', {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }

            await validateCart(result.data);

            setState({ loading: false, cartItems: result.data });
        };

        fetchCartItems();
    }, []);

    if (state.loading) return <></>
    else if (!state.cartItems || state.cartItems.length === 0) return <>
        <h3>Your shopping cart is currently empty :(</h3>
    </>
    else return <>
        <div className="mb-3">
            <h3>Summary</h3>
            <p>
                <b>Items in cart:</b> {state.cartItems.length}
            </p>

            <p>
                <b>Total price: </b>
                {
                    (() => {
                        let totalPrice = 0;
                        state.cartItems.forEach(item => totalPrice += item.cartItem.quantity * item.product.price);
                        return totalPrice.toFixed(2);
                    })()
                } PLN
            </p>

            <button className="btn btn-success" onClick={finalizeOnClick}>
                Finalize order
            </button>
        </div>

        <h3>Items in your shopping cart</h3>
        {
            state.cartItems.map(ci =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${ci.product.id}`}>
                    <p><b>
                        {ci.product.name}
                    </b></p>

                    <p>
                        {ci.product.description}
                    </p>

                    <p>Quantity: {ci.cartItem.quantity}</p>

                    <p>Price/Item: {ci.product.price.toFixed(2)}PLN</p>

                    <p>Total price: {ci.product.price.toFixed(2) * ci.cartItem.quantity} PLN</p>

                    <Link to={`/products/${ci.product.id}`} key={`prod-link-${ci.product.id}`}
                        className="btn btn-primary mr-2">
                        Go to product
                    </Link>

                    <button className="btn btn-danger" onClick={async () => await deleteFromCart(ci.cartItem.id, ci.product.id)}>
                        Remove from cart
                    </button>
                </div>
            )
        }

    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withCartAwareness()
    .build(CartPage);