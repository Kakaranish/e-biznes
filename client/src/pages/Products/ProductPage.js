import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { isValidUUID } from '../../common';
import ProductWishlistStatus from './ProductWishlistStatus';


const ProductPage = (props) => {

    const productId = props.match.params.id;

    const addToCartOnClick = (props) => {

    }

    const [state, setState] = useState({ loading: true, product: null });
    useEffect(() => {
        const fetchProduct = async () => {
            const result = await axios.get(`/api/products/${productId}`,
                { validateStatus: false });

            if (result.status === 404) {
                setState({ loading: false, product: null });
                return;
            }
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, product: result.data });
        };

        if (isValidUUID(productId)) fetchProduct();
    }, []);

    if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else {
        if (!state.product) return <h3>Product Id '{productId}' does not exist</h3>
        else return <>
            <h3>
                {state.product.product.name}
                <ProductWishlistStatus productId={productId} />
            </h3>

            <p>
                <b>Description:</b> {state.product.product.description}
            </p>

            <p>
                <b>Price:</b> {state.product.product.price.toFixed(2)}PLN
            </p>

            <p>
                <b>Available quantity:</b> {state.product.product.quantity}
            </p>

            <p>
                <b>Category:</b> {state.product.category?.name ?? 'None'}
            </p>

            <button type="button" className="btn btn-primary w-25 mr-2" onClick={addToCartOnClick}>
                Add to cart
			</button>
        </>
    }
};

export default ProductPage;