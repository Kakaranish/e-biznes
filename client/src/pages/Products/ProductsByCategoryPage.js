import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { doRequest } from '../../common/Utils';
import axios from 'axios';

const ProductsByCategoryPage = (props) => {

    const categoryId = props.match.params.id;

    const [state, setState] = useState({ loading: true, result: null });
    useEffect(() => {
        const fetchProducts = async () => {
            const action = async () => axios.get(`/api/products/category/${categoryId}`,
                { validateStatus: false });
            try {
                const result = await doRequest(action);
                setState({ loading: false, result: result });
            } catch (error) {
                setState({ loading: false, result: null });
                alert(`${error} error occured`);
            }
        };
        fetchProducts();
    }, []);

    if (state.loading) return <></>
    else {
        if (!state.result || !state.result.products?.length)
            return <h3>There are no products in category {state.result.category.name}</h3>
        else return <>
            <h3>Products in category {state.result.category.name}</h3>
            {
                state.result.products.map((item, i) =>
                    <div className="p-3" style={{ border: "1px solid gray" }} key={`div-${item.id}`}>
                        <p><b>{item.name}</b></p>

                        <p>Price: {item.price.toFixed(2)}PLN</p>

                        <Link to={`/products/${item.id}`} key={`link-${i}`} className="btn btn-success w-25">
                            Go to product
                        </Link>
                    </div>
                )
            }
        </>
    }
};

export default ProductsByCategoryPage;