import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { doRequest } from '../../common/Utils';

const ProductsPage = () => {
    const [state, setState] = useState({ loading: true, products: null });
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const action = async () => axios.get('/api/products',
                    { validateStatus: false });
                const result = await doRequest(action);
                setState({ loading: false, products: result });
            } catch (error) {
                setState({ loading: false, products: null });
                alert(`${error} error occured`);
            }
        }
        fetchProducts();
    }, []);

    if (state.loading) return <></>
    else {
        if (!state.products || !state.products.length)
            return <h3>There are no products to show</h3>
        else return <>
            <h3>All products</h3>
            {
                state.products.map((prod, i) =>
                    <div className="p-3" style={{ border: "1px solid gray" }} key={`div-${prod.product.id}`}>
                        <p><b>{prod.product.name}</b></p>

                        <p>Category: {prod.category.name}</p>

                        <p>Price: {prod.product.price.toFixed(2)}PLN</p>

                        <Link to={`/products/${prod.product.id}`} key={`link-${i}`} className="btn btn-success w-25">
                            Go to product
                        </Link>
                    </div>
                )
            }
        </>
    }
};

export default ProductsPage;