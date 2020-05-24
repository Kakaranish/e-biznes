import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import ProductWishlistStatus from './Products/ProductWishlistStatus';
import * as Utils from '../Utils';

const WishlistPage = (props) => {

    const [state, setState] = useState({ loading: true, wishItems: null });
    useEffect(() => {
        const fetchWishlist = async () => {
            const result = await axios.get('/api/wishlist', {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, wishItems: result.data });
        };

        fetchWishlist();
    }, []);

    if (state.loading) return <></>
    else if (!state.wishItems || state.wishItems.length === 0) return <>
        <h3>You have products on your wishlist</h3>
    </>
    else return <>
        <h3>Your wishlist</h3>

        {
            state.wishItems.map((item, i) =>
                <div className="p-3" style={{ border: "1px solid gray" }} key={`div-${item.product.id}`}>
                    <p>
                        <b>{item.product.name}</b>
                        <ProductWishlistStatus initState={true} productId={item.product.id} />
                    </p>

                    <p>
                        <b>Price: </b>{item.product.price.toFixed(2)} PLN
                    </p>

                    <Link to={`/products/${item.product.id}`} key={`link-${i}`} className="btn btn-success w-25">
                        Go to product
                    </Link>
                </div>
            )
        }
    </>
};

export default Utils.createAuthAwareComponent(WishlistPage);