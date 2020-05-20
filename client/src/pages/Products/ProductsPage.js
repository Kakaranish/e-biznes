import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ProductsPage = () => {
    const [state, setState] = useState({ loading: true, products: null });
    useEffect(() => {
        const fetchProducts = async () => {
            const result = await axios.get('/api/products', { validateStatus: false });
            if (result.status !== 200) {
                setState({ loading: false, products: null });
                alert('Some error occured');
                console.log(result);
                return;
            }
            console.log(result);
            setState({ loading: false, products: result.data });
        };
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
                    <div className="p-3" style={{ border: "1px solid gray" }}>
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