import React from 'react';
import { useHistory } from 'react-router-dom';
import { getFormDataJsonFromEvent } from '../../common';
import axios from 'axios';

const CreateCategoryPage = () => {
    const history = useHistory();

    const onSubmit = async event => {
        event.preventDefault();
        const formData = getFormDataJsonFromEvent(event);
        const result = await axios.post('/api/categories', formData,
            { validateStatus: false });
        if (result.status !== 200) {
            alert('Some error occured');
            return;
        }
        history.push('/categories');
    };

    return (
        <>
            <h3>Create category</h3>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <input name="name" type="text" className="form-control" id="nameInput" placeholder="Name..." required />
                </div>

                <button type="submit" className="btn btn-success w-25">
                    Submit
				</button>
            </form>
        </>
    );
};

export default CreateCategoryPage;